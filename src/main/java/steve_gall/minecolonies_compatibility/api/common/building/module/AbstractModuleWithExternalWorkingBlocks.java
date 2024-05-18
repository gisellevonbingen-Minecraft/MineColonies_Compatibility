package steve_gall.minecolonies_compatibility.api.common.building.module;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.research.util.ResearchConstants;
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;
import com.minecolonies.core.colony.buildings.modules.WorkerBuildingModule;
import com.minecolonies.core.entity.pathfinding.Pathfinding;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.PathJobFindWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.WorkingBlocksPathResult;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public abstract class AbstractModuleWithExternalWorkingBlocks extends AbstractCraftingBuildingModule.Custom implements IModuleWithExternalWorkingBlocks
{
	public static final String TAG_REGISTERED_POSITIONS = MineColoniesCompatibility.rl("registered_positions").toString();

	private final Set<BlockPos> workingPositions = new HashSet<>();
	private WorkingBlocksPathResult pathReuslt;

	public AbstractModuleWithExternalWorkingBlocks(JobEntry jobEntry)
	{
		super(jobEntry);
	}

	@Override
	public void deserializeNBT(CompoundTag compound)
	{
		super.deserializeNBT(compound);

		this.workingPositions.clear();
		this.workingPositions.addAll(BlockPosUtil.readPosListFromNBT(compound, TAG_REGISTERED_POSITIONS));
	}

	@Override
	public void serializeNBT(@NotNull CompoundTag compound)
	{
		super.serializeNBT(compound);

		BlockPosUtil.writePosListToNBT(compound, TAG_REGISTERED_POSITIONS, new ArrayList<>(this.workingPositions));
	}

	@Override
	public @NotNull PathJobFindWorkingBlocks<?> createWorkingBlocksFindPath(@Nullable AbstractEntityCitizen citizen)
	{
		var corners = this.building.getCorners();
		var colony = this.building.getColony();
		var job = new PathJobFindWorkingBlocks<>(colony.getWorld(), citizen != null ? citizen.blockPosition() : this.building.getID(), BoundingBox.fromCorners(corners.getA(), corners.getB()), citizen, this.createPathResult(citizen));

		if (citizen != null)
		{
			job.setPathingOptions(citizen.getNavigation().getPathingOptions());
		}
		else
		{
			var researchEffects = colony.getResearchManager().getResearchEffects();
			var options = job.getPathingOptions();
			options.setEnterDoors(true);
			options.setCanOpenDoors(true);
			options.setCanUseRails(researchEffects.getEffectStrength(ResearchConstants.RAILS) > 0);
			options.setCanClimbVines(researchEffects.getEffectStrength(ResearchConstants.VINES) > 0);
		}

		return job;
	}

	@Override
	public boolean requestFindWorkingBlocks(@Nullable AbstractEntityCitizen citizen)
	{
		if (this.pathReuslt == null)
		{
			this.pathReuslt = this.createWorkingBlocksFindPath(citizen).getResult();
			this.pathReuslt.startJob(Pathfinding.getExecutor());
			return true;
		}
		else if (this.pathReuslt.isDone())
		{
			this.workingPositions.addAll(this.pathReuslt.positions);
			this.pathReuslt = null;
			return false;
		}
		else
		{
			return true;
		}

	}

	protected abstract WorkingBlocksPathResult createPathResult(@Nullable AbstractEntityCitizen citizen);

	@Override
	public void onColonyTick(@NotNull IColony colony)
	{
		super.onColonyTick(colony);

		var data = this.building.getModuleMatching(WorkerBuildingModule.class, m -> m.getJobEntry() == this.jobEntry).getFirstCitizen();
		var worker = data != null ? data.getEntity().orElse(null) : null;
		this.requestFindWorkingBlocks(worker);
	}

	@Override
	public void onBlockPlacedInBuilding(@NotNull BlockState blockState, @NotNull BlockPos pos, @NotNull Level level)
	{
		this.addWorkingBlock(pos);
	}

	@Override
	public boolean addWorkingBlock(@NotNull BlockPos pos)
	{
		var level = this.building.getColony().getWorld();
		var state = level.getBlockState(pos);
		return this.isWorkingBlock(level, pos, state) && this.workingPositions.add(pos);
	}

	@Override
	public boolean removeWorkingBlock(@Nullable BlockPos pos)
	{
		return this.workingPositions.remove(pos);
	}

	@Override
	public List<BlockPos> getRegisteredBlocks()
	{
		return new ArrayList<>(this.workingPositions);
	}

}
