package steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist;

import static com.minecolonies.api.util.constant.CitizenConstants.BLOCK_BREAK_SOUND_RANGE;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.reflect.TypeToken;
import com.minecolonies.api.colony.buildings.modules.settings.ISettingKey;
import com.minecolonies.api.colony.requestsystem.requestable.StackList;
import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.entity.citizen.VisibleCitizenStatus;
import com.minecolonies.api.items.ModItems;
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.api.util.constant.StatisticsConstants;
import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.api.util.constant.translation.RequestSystemTranslationConstants;
import com.minecolonies.core.Network;
import com.minecolonies.core.colony.buildings.modules.BuildingModules;
import com.minecolonies.core.colony.buildings.modules.settings.BoolSetting;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingFarmer;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingLumberjack;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIInteract;
import com.minecolonies.core.entity.pathfinding.MinecoloniesAdvancedPathNavigate;
import com.minecolonies.core.entity.pathfinding.pathjobs.AbstractPathJob;
import com.minecolonies.core.network.messages.client.CompostParticleMessage;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraftforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.api.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.core.common.colony.job.JobOrchardist;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.pathfinding.FruitPathResult;
import steve_gall.minecolonies_compatibility.core.common.entity.pathfinding.PathJobFindFruit;

public class EntityAIWorkOrchardist extends AbstractEntityAIInteract<JobOrchardist, BuildingLumberjack>
{
	public static final VisibleCitizenStatus SEARCH = new VisibleCitizenStatus(new ResourceLocation(Constants.MOD_ID, "textures/icons/work/lumberjack_search.png"), "com.minecolonies.gui.visiblestatus.lumberjack_search");

	public static final ISettingKey<BoolSetting> FERTILIZE = BuildingFarmer.FERTILIZE;
	public static final List<Item> COMPOST_ITEMS = Arrays.asList(ModItems.compost, Items.BONE_MEAL);

	public static boolean isCompost(ItemStack stack)
	{
		return COMPOST_ITEMS.contains(stack.getItem());
	}

	@Nullable
	private FruitPathResult pathResult;
	private int searchIncrement = 0;
	private long nextSearchDelay = -1L;

	@SuppressWarnings({"unchecked", "rawtypes"})
	public EntityAIWorkOrchardist(@NotNull JobOrchardist job)
	{
		super(job);

		this.registerTargets(//
				new AITarget(AIWorkerState.IDLE, () -> AIWorkerState.START_WORKING, 1), //
				new AITarget(AIWorkerState.START_WORKING, this::decide, STANDARD_DELAY), //
				new AITarget(AIWorkerState.PREPARING, this::prepare, STANDARD_DELAY), //
				new AITarget(OrchardistAIState.SEARCH, this::search, STANDARD_DELAY), //
				new AITarget(OrchardistAIState.HARVEST, this::harvest, STANDARD_DELAY) //
		);
		this.worker.setCanPickUpLoot(true);
	}

	@Override
	public void tick()
	{
		if (this.nextSearchDelay > 0)
		{
			this.nextSearchDelay--;
		}

		super.tick();
	}

	@Override
	public Class<BuildingLumberjack> getExpectedBuildingClass()
	{
		return BuildingLumberjack.class;
	}

	@Override
	protected int getActionsDoneUntilDumping()
	{
		var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.orchardist;
		return config.actionsDoneUntilDumping.get().intValue();
	}

	private IAIState decide()
	{
		if (this.walkToBuilding())
		{
			return this.getState();
		}

		var building = this.building;
		var worker = this.worker;
		var amountOfCompostInBuilding = InventoryUtils.hasBuildingEnoughElseCount(building, EntityAIWorkOrchardist::isCompost, 1);
		var amountOfCompostInInv = InventoryUtils.getItemCountInItemHandler(worker.getInventoryCitizen(), EntityAIWorkOrchardist::isCompost);

		if (amountOfCompostInBuilding + amountOfCompostInInv <= 0)
		{
			var citizenData = worker.getCitizenData();

			if (building.getSetting(FERTILIZE).getValue().booleanValue() && !building.hasWorkerOpenRequestsOfType(citizenData.getId(), TypeToken.of(StackList.class)))
			{
				var compostAbleItems = COMPOST_ITEMS.stream().map(i -> new ItemStack(i, 1)).toList();
				citizenData.createRequestAsync(new StackList(compostAbleItems, RequestSystemTranslationConstants.REQUEST_TYPE_FERTILIZER, Constants.STACKSIZE, 1));
			}

		}
		else if (amountOfCompostInInv <= 0 && amountOfCompostInBuilding > 0)
		{
			this.needsCurrently = new Tuple<>(EntityAIWorkOrchardist::isCompost, Constants.STACKSIZE);
			return AIWorkerState.GATHERING_REQUIRED_MATERIALS;
		}

		return AIWorkerState.PREPARING;
	}

	private IAIState prepare()
	{
		if (this.nextSearchDelay > 0L)
		{
			return this.getState();
		}

		return OrchardistAIState.SEARCH;
	}

	/**
	 *
	 * @return false if we have the tool
	 */
	private boolean equipTool()
	{
		var toolType = ToolType.SHEARS;

		if (this.checkForToolOrWeapon(toolType))
		{
			return true;
		}

		var slot = CitizenHelper.getMaxLevelToolSlot(this.worker.getCitizenData(), toolType);
		this.worker.getCitizenItemHandler().setHeldItem(InteractionHand.MAIN_HAND, slot);
		return false;
	}

	private IAIState search()
	{
		if (this.equipTool())
		{
			return AIWorkerState.IDLE;
		}

		if (this.pathResult == null)
		{
			this.job.setFruit(null);
			this.worker.getCitizenData().setVisibleStatus(SEARCH);
			this.pathResult = this.creatNewPath();
		}

		if (this.pathResult.isDone())
		{
			return this.onPathDone();
		}
		else
		{
			return this.getState();
		}

	}

	private FruitPathResult creatNewPath()
	{
		var worker = this.worker;
		var building = this.building;
		var level = worker.level();

		var start = AbstractPathJob.prepareStart(worker);
		var buildingPos = building.getPosition();
		var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.orchardist;
		PathJobFindFruit job = null;

		if (building.shouldRestrict())
		{
			var startPos = building.getStartRestriction();
			var endPos = building.getEndRestriction();
			var furthestPos = BlockPosUtil.getFurthestCorner(start, startPos, endPos);

			job = new PathJobFindFruit(level, start, buildingPos, startPos, endPos, furthestPos, worker);
		}
		else
		{
			var current = config.searchRangeStart.get().intValue() + this.searchIncrement;
			job = new PathJobFindFruit(level, start, buildingPos, current, worker);
		}

		job.vertialRange = config.searchVerticalRange.get().intValue();
		job.needHarvestable = InventoryUtils.getItemCountInItemHandler(worker.getInventoryCitizen(), EntityAIWorkOrchardist::isCompost) == 0;
		return (FruitPathResult) ((MinecoloniesAdvancedPathNavigate) worker.getNavigation()).setPathJob(job, null, 1.0D, true);
	}

	private IAIState onPathDone()
	{
		var building = this.building;
		var fruit = this.pathResult.fruit;
		this.pathResult = null;

		if (fruit == null)
		{
			var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.orchardist;
			var start = config.searchRangeStart.get().intValue();
			var limit = config.searchRangeLimit.get().intValue();
			var current = start + this.searchIncrement;

			if (!building.shouldRestrict() && current < limit)
			{
				var increment = config.searchRangeIncrement.get().intValue();
				this.searchIncrement = Math.min(this.searchIncrement + increment, limit - start);
				this.setDelay(config.searchdelayBeforeIncrement.get().intValue());
				return this.getState();
			}
			else
			{
				this.searchIncrement = 0;
				this.nextSearchDelay = config.searchDelayAfterNotFound.get().intValue();
				return AIWorkerState.INVENTORY_FULL;
			}

		}
		else
		{
			this.searchIncrement = 0;
			this.job.setFruit(fruit);
			return OrchardistAIState.HARVEST;
		}

	}

	private IAIState harvest()
	{
		if (this.equipTool())
		{
			return AIWorkerState.IDLE;
		}

		var level = this.world;
		var worker = this.worker;
		var building = this.building;
		var job = this.job;
		var fruit = job.getFruit();

		if (fruit == null)
		{
			return OrchardistAIState.SEARCH;
		}

		var position = fruit.getPosition();

		if (building.shouldRestrict() && !BlockPosUtil.isInArea(building.getStartRestriction(), building.getEndRestriction(), position))
		{
			return OrchardistAIState.SEARCH;
		}

		if (this.walkToBlock(position))
		{
			return this.getState();
		}

		if (!fruit.updateAndIsValid(level))
		{
			return OrchardistAIState.SEARCH;
		}

		var inventory = worker.getInventoryCitizen();
		var hand = worker.getUsedItemHand();

		if (!fruit.canHarvest())
		{
			if (fruit.getState().getBlock() instanceof BonemealableBlock block)
			{
				if (!InventoryUtils.shrinkItemCountInItemHandler(inventory, EntityAIWorkOrchardist::isCompost))
				{
					return OrchardistAIState.SEARCH;
				}

				Network.getNetwork().sendToPosition(new CompostParticleMessage(position.above()), new PacketDistributor.TargetPoint(position.getX(), position.getY(), position.getZ(), BLOCK_BREAK_SOUND_RANGE, level.dimension()));
				worker.swing(hand);

				if (level instanceof ServerLevel serverLevel)
				{
					block.performBonemeal(serverLevel, serverLevel.random, fruit.getPosition(), fruit.getState());
				}

				return this.getState();
			}
			else
			{
				return OrchardistAIState.SEARCH;
			}

		}

		var drops = fruit.harvest(level);

		for (var stack : drops)
		{
			InventoryUtils.transferItemStackIntoNextBestSlotInItemHandler(stack, inventory);
		}

		worker.swing(hand);
		worker.getCitizenItemHandler().damageItemInHand(hand, 1);
		worker.getCitizenExperienceHandler().addExperience(XP_PER_BLOCK);

		this.incrementActionsDone();
		worker.decreaseSaturationForContinuousAction();

		this.onBlockDropReception(drops);
		this.setDelay(this.getLevelDelay());

		return OrchardistAIState.SEARCH;
	}

	private int getLevelDelay()
	{
		var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.orchardist;
		var reduce = (int) (this.getPrimarySkillLevel() * config.harvestDelayReducePerSkillLevel.get().doubleValue());
		return Math.max(1, config.harvestDelay.get().intValue() - reduce);
	}

	@Override
	public void onBlockDropReception(List<ItemStack> blockDrops)
	{
		super.onBlockDropReception(blockDrops);

		for (var stack : blockDrops)
		{
			this.building.getModule(BuildingModules.STATS_MODULE).incrementBy(StatisticsConstants.ITEM_OBTAINED + ";" + stack.getItem().getDescriptionId(), stack.getCount());
		}

	}

}
