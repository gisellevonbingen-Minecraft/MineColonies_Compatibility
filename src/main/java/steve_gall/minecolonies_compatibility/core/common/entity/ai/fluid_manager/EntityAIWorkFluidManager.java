package steve_gall.minecolonies_compatibility.core.common.entity.ai.fluid_manager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.entity.citizen.VisibleCitizenStatus;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.core.Network;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingDeliveryman;
import com.minecolonies.core.entity.ai.workers.crafting.AbstractEntityAICrafting;
import com.minecolonies.core.entity.pathfinding.PathfindingUtils;
import com.minecolonies.core.entity.pathfinding.navigation.MinecoloniesAdvancedPathNavigate;
import com.minecolonies.core.network.messages.client.LocalizedParticleEffectMessage;
import com.minecolonies.core.util.citizenutils.CitizenItemUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.pathfinding.MatchBlocksPathResult;
import steve_gall.minecolonies_compatibility.core.common.entity.pathfinding.PathJobFindLavaCauldron;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;
import steve_gall.minecolonies_compatibility.core.common.job.JobFluidManager;

public class EntityAIWorkFluidManager extends AbstractEntityAICrafting<JobFluidManager, BuildingDeliveryman>
{
	public static final VisibleCitizenStatus SEARCH = new VisibleCitizenStatus(new ResourceLocation(Constants.MOD_ID, "textures/icons/work/lumberjack_search.png"), "com.minecolonies.gui.visiblestatus.lumberjack_search");
	public static final double XP_PER_HARVEST = 0.5D;

	@Nullable
	private MatchBlocksPathResult pathResult;
	@Nullable
	private BlockPos cauldronPos;
	private long nextSearchDelay = -1L;
	private int pickupProgress = 0;

	@SuppressWarnings({"unchecked", "rawtypes"})
	public EntityAIWorkFluidManager(@NotNull JobFluidManager job)
	{
		super(job);
		this.registerTargets(//
				new AITarget(FluidManagerAIState.SEARCH, this::search, STANDARD_DELAY), //
				new AITarget(FluidManagerAIState.PICKUP, this::pickUp, STANDARD_DELAY) //
		);
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
	public Class<BuildingDeliveryman> getExpectedBuildingClass()
	{
		return BuildingDeliveryman.class;
	}

	@Override
	protected IAIState decide()
	{
		var state = super.decide();

		if (state != AIWorkerState.IDLE)
		{
			return state;
		}
		else if (this.nextSearchDelay > 0L)
		{
			return AIWorkerState.START_WORKING;
		}
		else if (this.walkToBuilding())
		{
			return this.getState();
		}

		return this.decideSearch(AIWorkerState.START_WORKING);
	}

	protected IAIState decideSearch(IAIState fallback)
	{
		if (InventoryUtils.getItemCountInItemHandler(this.worker.getInventoryCitizen(), item -> item.is(Items.BUCKET)) < 1)
		{
			this.checkIfRequestForItemExistOrCreateAsync(new ItemStack(Items.BUCKET), 16, 1);
			return fallback;
		}
		else
		{
			return FluidManagerAIState.SEARCH;
		}

	}

	protected IAIState search()
	{
		if (this.pathResult == null)
		{
			this.cauldronPos = null;
			this.pickupProgress = 0;
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

	protected MatchBlocksPathResult creatNewPath()
	{
		var level = this.world;
		var worker = this.worker;
		var building = this.building;

		var start = PathfindingUtils.prepareStart(worker);
		var buildingPos = building.getPosition();
		var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.fluidManager;
		PathJobFindLavaCauldron job = null;

		var restrictModule = building.getModule(ModBuildingModules.FLUID_MANAGER_LAVA_CAULDRON);

		if (restrictModule.isRestrictEnabled())
		{
			var restrictionBox = BoundingBox.fromCorners(restrictModule.getRestrictAreaPos1(), restrictModule.getRestrictAreaPos2());
			job = new PathJobFindLavaCauldron(level, start, restrictionBox, worker);
		}
		else
		{
			var range = config.searchRange.get().intValue();
			job = new PathJobFindLavaCauldron(level, start, buildingPos, range, worker);
		}

		return (MatchBlocksPathResult) ((MinecoloniesAdvancedPathNavigate) worker.getNavigation()).setPathJob(job, null, 1.0D, true);
	}

	protected IAIState onPathDone()
	{
		var positions = this.pathResult.positions;
		this.pathResult = null;

		if (positions.size() == 0)
		{
			var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.fluidManager;
			this.nextSearchDelay = config.searchDelayAfterNotFound.get().intValue();
			return AIWorkerState.INVENTORY_FULL;
		}
		else
		{
			this.cauldronPos = positions.get(0);
			return FluidManagerAIState.PICKUP;
		}

	}

	protected IAIState pickUp()
	{
		if (this.cauldronPos == null || this.world.getBlockState(this.cauldronPos).getBlock() != Blocks.LAVA_CAULDRON)
		{
			return AIWorkerState.START_WORKING;
		}

		var inventory = this.getInventory();
		var slot = InventoryUtils.findFirstSlotInItemHandlerWith(inventory, stack -> stack.is(Items.BUCKET));

		if (slot == -1)
		{
			return AIWorkerState.START_WORKING;
		}

		CitizenItemUtils.setHeldItem(this.worker, InteractionHand.MAIN_HAND, slot);

		if (this.walkToWorkPos(this.cauldronPos))
		{
			return this.getState();
		}

		var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.fluidManager;
		var delay = config.pickupDelay.get().intValue() - (int) (this.getSecondarySkillLevel() * config.pickupDelayReducePerSkillLevel.get().doubleValue());

		this.hitBlockWithToolInHand(this.cauldronPos);
		Network.getNetwork().sendToTrackingEntity(new LocalizedParticleEffectMessage(new ItemStack(Items.LAVA_BUCKET), this.cauldronPos), this.worker);

		if (this.pickupProgress < delay)
		{
			this.pickupProgress += STANDARD_DELAY;
			return this.getState();
		}

		this.world.setBlock(this.cauldronPos, Blocks.CAULDRON.defaultBlockState(), Block.UPDATE_ALL);
		this.world.playSound(null, this.cauldronPos, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);

		inventory.getStackInSlot(slot).shrink(1);
		InventoryUtils.transferItemStackIntoNextBestSlotInItemHandler(new ItemStack(Items.LAVA_BUCKET), inventory);
		CitizenItemUtils.setHeldItem(this.worker, InteractionHand.MAIN_HAND, slot);

		this.worker.getCitizenExperienceHandler().addExperience(XP_PER_HARVEST);
		this.incrementActionsDone();
		this.worker.decreaseSaturationForContinuousAction();

		return this.decideSearch(AIWorkerState.INVENTORY_FULL);
	}

}
