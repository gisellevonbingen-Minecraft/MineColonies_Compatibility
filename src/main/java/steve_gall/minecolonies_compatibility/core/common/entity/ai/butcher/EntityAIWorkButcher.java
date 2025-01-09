package steve_gall.minecolonies_compatibility.core.common.entity.ai.butcher;

import static com.minecolonies.api.util.constant.CitizenConstants.BLOCK_BREAK_PARTICLE_RANGE;
import static com.minecolonies.api.util.constant.CitizenConstants.FACING_DELTA_YAW;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.interactionhandling.ChatPriority;
import com.minecolonies.api.colony.interactionhandling.InteractionValidatorRegistry;
import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.core.Network;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.interactionhandling.StandardInteraction;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIInteract;
import com.minecolonies.core.entity.pathfinding.PathfindingUtils;
import com.minecolonies.core.entity.pathfinding.navigation.MinecoloniesAdvancedPathNavigate;
import com.minecolonies.core.network.messages.client.BlockParticleEffectMessage;
import com.minecolonies.core.util.citizenutils.CitizenItemUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.api.common.butcher.Butcherable;
import steve_gall.minecolonies_compatibility.api.common.butcher.CustomizedButcherable;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.pathfinding.ButcherPositionsPathResult;
import steve_gall.minecolonies_compatibility.core.common.entity.pathfinding.PathJobFindButcherPosition;
import steve_gall.minecolonies_compatibility.core.common.job.JobButcher;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.CustomizableDeliverable;

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
		var amountInBuilding = InventoryUtils.hasBuildingEnoughElseCount(building, CustomizedButcherable::isButcherable, 1);
		var amountInInv = InventoryUtils.getItemCountInItemHandler(worker.getInventoryCitizen(), CustomizedButcherable::isButcherable);

		if (amountInBuilding + amountInInv <= 0)
		{
			var citizenData = worker.getCitizenData();

			if (!CitizenHelper.isRequested(citizenData, CustomizableDeliverable.TYPE_TOKEN, r -> r.getRequest().getObject() instanceof Butcherable))
			{
				citizenData.createRequestAsync(new CustomizableDeliverable(new Butcherable(1)));
			}

		}
		else if (amountInInv <= 0 && amountInBuilding > 0)
		{
			this.needsCurrently = new Tuple<>(CustomizedButcherable::isButcherable, Constants.STACKSIZE);
			return AIWorkerState.GATHERING_REQUIRED_MATERIALS;
		}

		if (this.nextSearchDelay > 0L)
		{
			return this.getState();
		}

		return ButcherAIState.SEARCH;
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
		return (ButcherPositionsPathResult) ((MinecoloniesAdvancedPathNavigate) worker.getNavigation()).setPathJob(job, null, 1.0D, true);
	}

	private IAIState onPathDone()
	{
		var result = this.pathResult;
		this.pathResult = null;
		this.butcherProgress = 0;
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
	private ButcherInfo getButcheringBlock(BlockPos pos)
	{
		var state = this.world.getBlockState(pos);
		var butcherable = CustomizedButcherable.selectByButcheringBlock(this.world, pos, state);

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
		var state = this.world.getBlockState(position);

		for (var i = 0; i < inventory.getSlots(); i++)
		{
			var item = inventory.getStackInSlot(i);
			var butcherable = CustomizedButcherable.selectByItem(item);

			if (butcherable != null && !butcherable.isTableBlock(this.world, position, state))
			{
				return butcherable;
			}

		}

		return null;
	}

	@Nullable
	private ButcherInfo getButcherTable(BlockPos pos)
	{
		var inventory = this.worker.getInventoryCitizen();
		var state = this.world.getBlockState(pos);

		for (var i = 0; i < inventory.getSlots(); i++)
		{
			var item = inventory.getStackInSlot(i);
			var butcherable = CustomizedButcherable.selectByItem(item);

			if (butcherable != null && butcherable.isTableBlock(this.world, pos, state))
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
		public EquipmentTypeEntry getToolType(Level level, BlockPos position, BlockState state)
		{
			if (this.isBlock())
			{
				return this.butcherable().getBlockToolType(level, position, state);
			}
			else
			{
				return this.butcherable().getTableToolType(level, position, state);
			}

		}

		public SoundEvent getSound(Level level, BlockPos position, BlockState state)
		{
			if (this.isBlock())
			{
				return this.butcherable().getBlockSound(level, position, state);
			}
			else
			{
				return this.butcherable().getTableSound(level, position, state);
			}

		}

		public void doButcher(Level level, BlockPos position, BlockState state, AbstractEntityCitizen worker, InteractionHand itemHand)
		{
			if (this.isBlock())
			{
				this.butcherable().doButcherBlock(level, position, state, worker);
			}
			else
			{
				this.butcherable().doButcherTable(level, position, state, worker, itemHand);
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

		var state = level.getBlockState(position);
		var toolType = info.getToolType(level, position, state);

		if (this.equipTool(toolType))
		{
			return AIWorkerState.START_WORKING;
		}
		else if (!this.walkToWorkPos(position))
		{
			return this.getState();
		}

		var itemHand = toolType != ModEquipmentTypes.none.get() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
		CitizenItemUtils.setHeldItem(worker, itemHand, info.slot());

		var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.butcher;
		var delay = config.workDelay.get() - (int) ((this.getPrimarySkillLevel() + this.getSecondarySkillLevel()) * config.workDelayReducePerSkillLevel.get().doubleValue());

		this.hitBlockWithToolInHand(position);
		worker.queueSound(info.getSound(level, position, state), position, 1, 0);

		if (this.butcherProgress < delay)
		{
			this.butcherProgress += STANDARD_DELAY;
			return this.getState();
		}

		this.butcherProgress = 0;
		info.doButcher(level, position, state, worker, itemHand);

		if (toolType != ModEquipmentTypes.none.get())
		{
			CitizenItemUtils.damageItemInHand(worker, InteractionHand.MAIN_HAND, 1);
		}

		CitizenItemUtils.setHeldItem(worker, itemHand, info.slot());
		worker.getCitizenExperienceHandler().addExperience(XP_PER_HARVEST);

		if (this.getButcheringBlock(position) != null)
		{
			worker.decreaseSaturationForContinuousAction();
			return this.getState();
		}
		else
		{
			this.incrementActionsDoneAndDecSaturation();
			this.setDelay(20);
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

	private boolean equipTool(EquipmentTypeEntry toolType)
	{
		if (toolType == ModEquipmentTypes.none.get())
		{
			CitizenItemUtils.setHeldItem(this.worker, InteractionHand.MAIN_HAND, -1);
			return false;
		}
		else if (this.checkForToolOrWeapon(toolType))
		{
			return true;
		}

		var slot = CitizenHelper.getMaxLevelToolSlot(this.worker.getCitizenData(), toolType);
		CitizenItemUtils.setHeldItem(this.worker, InteractionHand.MAIN_HAND, slot);
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
