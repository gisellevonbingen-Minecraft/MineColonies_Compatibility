package steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist;

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
import com.minecolonies.api.util.constant.CitizenConstants;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.StatisticsConstants;
import com.minecolonies.api.util.constant.translation.RequestSystemTranslationConstants;
import com.minecolonies.core.Network;
import com.minecolonies.core.colony.buildings.modules.BuildingModules;
import com.minecolonies.core.colony.buildings.modules.settings.BoolSetting;
import com.minecolonies.core.colony.buildings.modules.settings.SettingKey;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingFarmer;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingLumberjack;
import com.minecolonies.core.entity.ai.basic.AbstractEntityAIInteract;
import com.minecolonies.core.entity.pathfinding.MinecoloniesAdvancedPathNavigate;
import com.minecolonies.core.entity.pathfinding.pathjobs.AbstractPathJob;
import com.minecolonies.core.network.messages.client.CompostParticleMessage;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.pathfinding.FruitPathResult;
import steve_gall.minecolonies_compatibility.core.common.entity.pathfinding.PathJobFindFruit;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;
import steve_gall.minecolonies_compatibility.core.common.job.JobOrchardist;

public class EntityAIWorkOrchardist extends AbstractEntityAIInteract<JobOrchardist, BuildingLumberjack>
{
	public static final VisibleCitizenStatus SEARCH = new VisibleCitizenStatus(new ResourceLocation(Constants.MOD_ID, "textures/icons/work/lumberjack_search.png"), "com.minecolonies.gui.visiblestatus.lumberjack_search");

	public static final double XP_PER_HARVEST = 0.5D;
	public static final ISettingKey<BoolSetting> FERTILIZE = BuildingFarmer.FERTILIZE;
	public static final ISettingKey<BoolSetting> NEED_MAX_HARVEST = new SettingKey<>(BoolSetting.class, MineColoniesCompatibility.rl("need_max_harvest"));
	public static final List<Item> COMPOST_ITEMS = Arrays.asList(ModItems.compost, Items.BONE_MEAL);

	public static boolean isCompost(ItemStack stack)
	{
		return COMPOST_ITEMS.contains(stack.getItem());
	}

	@Nullable
	private FruitPathResult pathResult;
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
	private boolean equipTool(IToolType toolType)
	{
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
		if (this.pathResult == null)
		{
			this.job.setFruit(null);
			this.worker.getCitizenData().setVisibleStatus(SEARCH);
			this.pathResult = this.creatNewPath();
			return this.getState();
		}
		else if (this.pathResult.isDone())
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
		var level = this.world;
		var worker = this.worker;
		var building = this.building;

		var start = AbstractPathJob.prepareStart(worker);
		var buildingPos = building.getPosition();
		var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.orchardist;
		PathJobFindFruit job = null;

		if (building.shouldRestrict())
		{
			var restrictionBox = BoundingBox.fromCorners(building.getStartRestriction(), building.getEndRestriction());
			job = new PathJobFindFruit(level, start, restrictionBox, worker);
		}
		else
		{
			var range = config.searchRange.get().intValue();
			job = new PathJobFindFruit(level, start, buildingPos, range, worker);
		}

		job.vertialRange = config.searchVerticalRange.get().intValue();
		job.needHarvestable = InventoryUtils.getItemCountInItemHandler(worker.getInventoryCitizen(), EntityAIWorkOrchardist::isCompost) == 0;
		job.needMaxHarvest = building.getSetting(NEED_MAX_HARVEST).getValue().booleanValue();
		job.exceptFruits.addAll(building.getModule(ModBuildingModules.FRUITLIST_BLACKLIST).getIds());
		return (FruitPathResult) ((MinecoloniesAdvancedPathNavigate) worker.getNavigation()).setPathJob(job, null, 1.0D, true);
	}

	private IAIState onPathDone()
	{
		var fruit = this.pathResult.fruit;
		this.pathResult = null;

		if (fruit == null)
		{
			var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.orchardist;
			this.nextSearchDelay = config.searchDelayAfterNotFound.get().intValue();
			return AIWorkerState.INVENTORY_FULL;
		}
		else
		{
			this.job.setFruit(fruit);
			return OrchardistAIState.HARVEST;
		}

	}

	private IAIState harvest()
	{
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
		else if (!fruit.updateAndIsValid(level))
		{
			return OrchardistAIState.SEARCH;
		}
		else if (building.getModule(ModBuildingModules.FRUITLIST_BLACKLIST).containsId(fruit.getFruit().getId()))
		{
			return AIWorkerState.START_WORKING;
		}
		else if (this.equipTool(fruit.getToolType()))
		{
			return AIWorkerState.START_WORKING;
		}
		else if (this.walkToBlock(position) || this.hasNotDelayed(this.getLevelDelay()))
		{
			return this.getState();
		}

		var inventory = worker.getInventoryCitizen();
		var hand = worker.getUsedItemHand();
		var state = fruit.getContext().getState();

		if (!fruit.canHarvest(building.getSetting(NEED_MAX_HARVEST).getValue().booleanValue()))
		{
			if (state.getBlock() instanceof BonemealableBlock block && block.isValidBonemealTarget(level, position, state, level.isClientSide))
			{
				if (!InventoryUtils.shrinkItemCountInItemHandler(inventory, EntityAIWorkOrchardist::isCompost))
				{
					return OrchardistAIState.SEARCH;
				}

				worker.swing(hand);

				if (block.isBonemealSuccess(level, level.random, position, state))
				{
					Network.getNetwork().sendToPosition(new CompostParticleMessage(position), new PacketDistributor.TargetPoint(position.getX(), position.getY(), position.getZ(), CitizenConstants.BLOCK_BREAK_SOUND_RANGE, level.dimension()));

					if (level instanceof ServerLevel serverLevel)
					{
						block.performBonemeal(serverLevel, serverLevel.random, position, state);
					}

				}

				return this.getState();
			}
			else
			{
				return OrchardistAIState.SEARCH;
			}

		}

		var harvester = new HarvesterContext(worker, inventory.getHeldItem(hand));
		var drops = fruit.harvest(harvester);
		level.playSound(null, position, fruit.getSound(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);

		for (var stack : drops)
		{
			InventoryUtils.transferItemStackIntoNextBestSlotInItemHandler(stack, inventory);
		}

		worker.swing(hand);
		worker.getCitizenItemHandler().damageItemInHand(hand, 1);

		var colony = worker.getCitizenColonyHandler().getColony();
		colony.getStatisticsManager().increment(StatisticsConstants.CROPS_HARVESTED, colony.getDay());
		worker.getCitizenExperienceHandler().addExperience(XP_PER_HARVEST);

		this.incrementActionsDone();
		worker.decreaseSaturationForContinuousAction();

		this.onBlockDropReception(drops);

		if (fruit.updateAndIsValid(level))
		{
			return this.getState();
		}

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
