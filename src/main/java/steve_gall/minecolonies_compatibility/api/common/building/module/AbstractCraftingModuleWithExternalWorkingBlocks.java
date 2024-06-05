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
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;
import com.minecolonies.core.colony.buildings.modules.WorkerBuildingModule;
import com.minecolonies.core.entity.pathfinding.Pathfinding;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.WorkingBlocksPathResult;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public abstract class AbstractCraftingModuleWithExternalWorkingBlocks extends AbstractCraftingBuildingModule.Custom implements ICraftingModuleWithExternalWorkingBlocks
{
	public static final String TAG_REGISTERED_POSITIONS = MineColoniesCompatibility.rl("registered_positions").toString();

	private final Set<BlockPos> workingPositions = new HashSet<>();
	private WorkingBlocksPathResult pathReuslt;

	public AbstractCraftingModuleWithExternalWorkingBlocks(JobEntry jobEntry)
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

	@Override
	public void onColonyTick(@NotNull IColony colony)
	{
		super.onColonyTick(colony);

		var data = this.building.getModuleMatching(WorkerBuildingModule.class, m -> m.getJobEntry() == this.jobEntry).getFirstCitizen();
		var worker = data != null ? data.getEntity().orElse(null) : null;
		this.requestFindWorkingBlocks(worker);
	}

	@Override
	public boolean isWorkingBlock(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state)
	{
		return this.isIntermediate(state.getBlock());
	}

	@Override
	public abstract boolean isIntermediate(@NotNull Block block);

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
