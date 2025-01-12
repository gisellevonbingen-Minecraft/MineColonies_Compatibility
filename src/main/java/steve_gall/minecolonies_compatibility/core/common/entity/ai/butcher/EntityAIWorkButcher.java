package steve_gall.minecolonies_compatibility.core.common.entity.ai.butcher;

import static com.minecolonies.api.util.constant.CitizenConstants.BLOCK_BREAK_PARTICLE_RANGE;
import static com.minecolonies.api.util.constant.CitizenConstants.FACING_DELTA_YAW;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.interactionhandling.ChatPriority;
import com.minecolonies.api.colony.interactionhandling.InteractionValidatorRegistry;
import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.api.util.constant.TypeConstants;
import com.minecolonies.core.Network;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.interactionhandling.StandardInteraction;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIInteract;
import com.minecolonies.core.entity.pathfinding.PathfindingUtils;
import com.minecolonies.core.entity.pathfinding.navigation.EntityNavigationUtils;
import com.minecolonies.core.entity.pathfinding.navigation.MinecoloniesAdvancedPathNavigate;
import com.minecolonies.core.network.messages.client.BlockParticleEffectMessage;
import com.minecolonies.core.util.citizenutils.CitizenItemUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherBlockContext;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherCitizenContext;
import steve_gall.minecolonies_compatibility.api.common.butcher.CustomizedButcherable;
import steve_gall.minecolonies_compatibility.api.common.crafting.IngredientStack;
import steve_gall.minecolonies_compatibility.api.common.crafting.ToolOrIngredientStack;
import steve_gall.minecolonies_compatibility.api.common.requestsystem.IngredientDeliverable;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.pathfinding.ButcherPositionsPathResult;
import steve_gall.minecolonies_compatibility.core.common.entity.pathfinding.PathJobFindButcherPosition;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.core.common.job.JobButcher;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.CustomizableDeliverable;
import steve_gall.minecolonies_tweaks.api.common.tool.CustomToolType;

public class EntityAIWorkButcher extends AbstractEntityAIInteract<JobButcher, AbstractBuilding>
{
	public static final double XP_PER_HARVEST = 0.5D;
	public static final Component TABLE_NEEDED_KEY = Component.literal(MineColoniesCompatibility.tl("butcer.table_needed"));

	static
	{
		InteractionValidatorRegistry.registerStandardPredicate(TABLE_NEEDED_KEY, c -> c.getJob() instanceof JobButcher job && job.getTableNeeded() != null);
	}

	@Nullable
	private ButcherPositionsPathResult pathResult;
	private long nextSearchDelay = -1L;

	private BlockPos butcherPosition = null;
	private int butcherProgress = 0;
	private boolean walking = false;

	@SuppressWarnings({"unchecked", "rawtypes"})
	public EntityAIWorkButcher(@NotNull JobButcher job)
	{
		super(job);

		this.registerTargets(//
				new AITarget(AIWorkerState.IDLE, () -> AIWorkerState.START_WORKING, 1), //
				new AITarget(AIWorkerState.START_WORKING, this::decide, STANDARD_DELAY), //
				new AITarget(ButcherAIState.SEARCH, this::search, STANDARD_DELAY), //
				new AITarget(ButcherAIState.BUTCHER, this::butcher, STANDARD_DELAY) //
		);
		this.worker.setCanPickUpLoot(true);
	}

	@Override
	public void tick()
	{
		if (this.nextSearchDelay > 0)
		{
			this.nextSearchDelay -= AbstractEntityCitizen.ENTITY_AI_TICKRATE;
		}

		super.tick();
	}

	@Override
	public Class<AbstractBuilding> getExpectedBuildingClass()
	{
		return AbstractBuilding.class;
	}

	private IAIState decide()
	{
		if (!this.walkToBuilding())
		{
			return this.getState();
		}

		var building = this.building;
		var worker = this.worker;
		var amountInBuilding = InventoryUtils.hasBuildingEnoughElseCount(building, this::testButcherable, 1);
		var amountInInv = InventoryUtils.getItemCountInItemHandler(worker.getInventoryCitizen(), this::testButcherable);

		if (amountInBuilding + amountInInv <= 0)
		{
			var citizenData = worker.getCitizenData();
			var any = false;

			for (var request : CitizenHelper.getRequests(citizenData, CustomizableDeliverable.TYPE_TOKEN, r -> r.getRequest().getObject() instanceof Butcherable))
			{
				var butcherable = (Butcherable) request.getRequest().getObject();

				if (butcherable.getBlacklist() == null)
				{
					citizenData.getColony().getRequestManager().updateRequestState(request.getId(), RequestState.CANCELLED);
				}
				else
				{
					any = true;
				}

			}

			if (!any)
			{
				var blacklist = building.getModule(ModBuildingModules.BUTCHERABLELIST_BLACKLIST);
				citizenData.createRequestAsync(new CustomizableDeliverable(new Butcherable(1, blacklist)));
			}

		}
		else if (amountInInv <= 0 && amountInBuilding > 0)
		{
			this.needsCurrently = new Tuple<>(this::testButcherable, 8);
			return AIWorkerState.GATHERING_REQUIRED_MATERIALS;
		}

		if (this.nextSearchDelay > 0L)
		{
			return this.getState();
		}

		return ButcherAIState.SEARCH;
	}

	private boolean testButcherable(ItemStack item)
	{
		return this.selectByItem(item) != null;
	}

	private CustomizedButcherable selectByItem(ItemStack item)
	{
		var butcherable = CustomizedButcherable.selectByItem(item);

		if (butcherable != null && !this.building.getModule(ModBuildingModules.BUTCHERABLELIST_BLACKLIST).containsId(butcherable.getId()))
		{
			return butcherable;
		}
		else
		{
			return null;
		}

	}

	private IAIState search()
	{
		if (this.pathResult == null)
		{
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

	private @Nullable ButcherPositionsPathResult creatNewPath()
	{
		var worker = this.worker;
		var start = PathfindingUtils.prepareStart(worker);
		var corners = this.building.getCorners();
		var job = new PathJobFindButcherPosition(this.world, start, BoundingBox.fromCorners(corners.getA(), corners.getB()), worker);
		job.vertialRange = 2;
		job.exceptButcherables.addAll(this.building.getModule(ModBuildingModules.BUTCHERABLELIST_BLACKLIST).getIds());
		return (ButcherPositionsPathResult) ((MinecoloniesAdvancedPathNavigate) worker.getNavigation()).setPathJob(job, null, 1.0D, true);
	}

	private IAIState onPathDone()
	{
		var result = this.pathResult;
		this.pathResult = null;
		this.butcherProgress = 0;
		this.walking = false;
		this.job.setTableNeeded(null);

		for (var block : result.blocks)
		{
			this.butcherPosition = block;
			return ButcherAIState.BUTCHER;
		}

		for (var positon : result.tables)
		{
			var info = this.getButcherTable(positon);

			if (info != null)
			{
				this.butcherPosition = positon;
				return ButcherAIState.BUTCHER;
			}

		}

		for (var positon : result.tables)
		{
			var info = this.getNeededTable(positon);

			if (info != null)
			{
				this.job.setTableNeeded(info);
				this.worker.getCitizenData().triggerInteraction(new StandardInteraction(info.getTableNotFoundMessage(), TABLE_NEEDED_KEY, ChatPriority.BLOCKING));
				break;
			}

		}

		var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.fluidManager;
		this.nextSearchDelay = config.searchDelayAfterNotFound.get().intValue();
		return AIWorkerState.INVENTORY_FULL;
	}

	@Nullable
	private ButcherInfo getButcheringBlock(BlockPos position)
	{
		var context = new ButcherBlockContext(this.world, position, this.world.getBlockState(position));
		var butcherable = CustomizedButcherable.selectByButcheringBlock(context);

		if (butcherable != null)
		{
			return new ButcherInfo(butcherable, true, -1);
		}
		else
		{
			return null;
		}

	}

	private CustomizedButcherable getNeededTable(BlockPos position)
	{
		var inventory = this.worker.getInventoryCitizen();
		var context = new ButcherBlockContext(this.world, position, this.world.getBlockState(position));

		for (var i = 0; i < inventory.getSlots(); i++)
		{
			var item = inventory.getStackInSlot(i);
			var butcherable = this.selectByItem(item);

			if (butcherable != null && !butcherable.isTableBlock(context))
			{
				return butcherable;
			}

		}

		return null;
	}

	@Nullable
	private ButcherInfo getButcherTable(BlockPos position)
	{
		var inventory = this.worker.getInventoryCitizen();
		var context = new ButcherBlockContext(this.world, position, this.world.getBlockState(position));

		for (var i = 0; i < inventory.getSlots(); i++)
		{
			var item = inventory.getStackInSlot(i);
			var butcherable = this.selectByItem(item);

			if (butcherable != null && butcherable.isTableBlock(context))
			{
				return new ButcherInfo(butcherable, false, i);
			}

		}

		return null;
	}

	@Nullable
	private ButcherInfo getButcherInfo(BlockPos pos)
	{
		var butcheringBlock = this.getButcheringBlock(pos);

		if (butcheringBlock != null)
		{
			return butcheringBlock;
		}
		else
		{
			return this.getButcherTable(pos);
		}

	}

	private record ButcherInfo(CustomizedButcherable butcherable, boolean isBlock, int slot)
	{
		public ToolOrIngredientStack getTool(ButcherBlockContext context, @NotNull ButcherCitizenContext citizen)
		{
			if (this.isBlock())
			{
				return this.butcherable().getBlockTool(context, citizen);
			}
			else
			{
				return this.butcherable().getTableTool(context, citizen);
			}

		}

		public SoundEvent getSound(ButcherBlockContext context)
		{
			if (this.isBlock())
			{
				return this.butcherable().getBlockSound(context);
			}
			else
			{
				return this.butcherable().getTableSound(context);
			}

		}

		public void doButcher(ButcherBlockContext context, ButcherCitizenContext citizen, InteractionHand toolHand)
		{
			if (this.isBlock())
			{
				this.butcherable().doButcherBlock(context, citizen);
			}
			else
			{
				this.butcherable().doButcherTable(context, citizen, toolHand);
			}

		}

	}

	private IAIState butcher()
	{
		var level = this.world;
		var worker = this.worker;
		var position = this.butcherPosition;

		if (position == null)
		{
			return AIWorkerState.START_WORKING;
		}

		var info = this.getButcherInfo(position);

		if (info == null)
		{
			return AIWorkerState.START_WORKING;
		}

		var blockContext = new ButcherBlockContext(level, position, level.getBlockState(position));
		var citizenContext = new ButcherCitizenContext(this, worker);
		var toolType = info.getTool(blockContext, citizenContext);

		if (this.equipTool(toolType))
		{
			return AIWorkerState.START_WORKING;
		}
		else if (!EntityNavigationUtils.walkToPosInBuilding(worker, position, this.building, this.walking ? EntityNavigationUtils.WOKR_IN_BUILDING_DIST : 0))
		{
			this.walking = true;
			return this.getState();
		}

		var itemHand = !toolType.isEmpty() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		CitizenItemUtils.setHeldItem(worker, itemHand, info.slot());

		var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.butcher;
		var delay = config.workDelay.get() - (int) ((this.getPrimarySkillLevel() + this.getSecondarySkillLevel()) * config.workDelayReducePerSkillLevel.get().doubleValue());

		this.hitBlockWithToolInHand(position);
		worker.queueSound(info.getSound(blockContext), position, 1, 0);

		if (this.butcherProgress < delay)
		{
			this.butcherProgress += STANDARD_DELAY;
			return this.getState();
		}

		this.butcherProgress = 0;
		info.doButcher(blockContext, citizenContext, itemHand);

		CitizenItemUtils.setHeldItem(worker, itemHand, info.slot());
		worker.getCitizenExperienceHandler().addExperience(XP_PER_HARVEST);

		if (this.getButcheringBlock(position) != null)
		{
			this.setDelay(10);
			worker.decreaseSaturationForContinuousAction();
			return this.getState();
		}
		else
		{
			this.setDelay(20);
			this.incrementActionsDoneAndDecSaturation();
		}

		if (this.job.getActionsDone() >= this.getActionsDoneUntilDumping())
		{
			return AIWorkerState.INVENTORY_FULL;
		}
		else if (this.getButcherTable(position) != null)
		{
			return this.getState();
		}
		else
		{
			return AIWorkerState.START_WORKING;
		}

	}

	private boolean equipTool(ToolOrIngredientStack toolType)
	{
		var slot = -1;

		if (toolType.isEmpty())
		{
			slot = -1;
		}
		else if (toolType.isToolType())
		{
			if (this.checkForToolOrWeapon(toolType.toolType()))
			{
				return true;
			}

			slot = CitizenHelper.getMaxLevelToolSlot(this.worker.getCitizenData(), toolType.toolType());
		}
		else
		{
			var stack = toolType.stack();

			if (!this.checkIfRequestForItemExistOrCreate(stack, CustomToolType.getFallbackTranslationKey(ModToolTypes.BUTCHER_TOOL.getName())))
			{
				return true;
			}

			slot = InventoryUtils.findFirstSlotInItemHandlerWith(this.getInventory(), stack::testType);
		}

		CitizenItemUtils.setHeldItem(this.worker, InteractionHand.MAIN_HAND, slot);
		return false;
	}

	private boolean checkIfRequestForItemExistOrCreate(IngredientStack stack, String description)
	{
		var worker = this.worker;
		var building = this.building;
		var invCount = InventoryUtils.getItemCountInItemHandler(worker.getInventoryCitizen(), stack::testType);

		if (invCount >= stack.count())
		{
			return true;
		}
		else if (!this.walkToBuilding())
		{
			return false;
		}

		var updatedCount = stack.count() - invCount;

		if (InventoryUtils.hasBuildingEnoughElseCount(building, stack::testType, updatedCount) >= updatedCount)
		{
			if (InventoryUtils.transferXOfFirstSlotInProviderWithIntoNextFreeSlotInItemHandler(building, stack::testType, updatedCount, worker.getInventoryCitizen()))
			{
				return true;
			}

		}

		var deliverable = new IngredientDeliverable(stack.ingredient(), description, stack.count());

		if (!CitizenHelper.isRequested(worker.getCitizenData(), TypeConstants.DELIVERABLE, r -> this.testDeliverable(r.getRequest(), deliverable)))
		{
			worker.getCitizenData().createRequest(new CustomizableDeliverable(deliverable));
		}

		return false;
	}

	private boolean testDeliverable(IDeliverable deliverable, IngredientDeliverable deliverable2)
	{
		if (deliverable instanceof CustomizableDeliverable customizable)
		{
			if (customizable.getObject() instanceof IngredientDeliverable other)
			{
				return other.getIngredient().getStackingIds().equals(deliverable2.getIngredient().getStackingIds());
			}

		}

		return false;
	}

	private void hitBlockWithToolInHand(BlockPos pos)
	{
		var worker = this.worker;
		worker.getLookControl().setLookAt(pos.getX(), pos.getY(), pos.getZ(), FACING_DELTA_YAW, worker.getMaxHeadXRot());
		worker.swing(InteractionHand.MAIN_HAND);

		var blockState = worker.level().getBlockState(pos);
		var vector = pos.subtract(worker.blockPosition());
		var facing = BlockPosUtil.directionFromDelta(vector.getX(), vector.getY(), vector.getZ()).getOpposite();

		Network.getNetwork().sendToPosition(new BlockParticleEffectMessage(pos, blockState, facing.ordinal()), new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), BLOCK_BREAK_PARTICLE_RANGE, worker.level().dimension()));
	}

}
