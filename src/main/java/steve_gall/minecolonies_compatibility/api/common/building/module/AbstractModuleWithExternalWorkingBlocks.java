package steve_gall.minecolonies_compatibility.api.common.building.module;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import com.minecolonies.api.colony.buildings.modules.ITickingModule;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.core.entity.pathfinding.Pathfinding;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.WorkingBlocksPathResult;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public abstract class AbstractModuleWithExternalWorkingBlocks extends AbstractBuildingModule implements IModuleWithExternalWorkingBlocks, ITickingModule, IPersistentModule
{
	public static final String TAG_REGISTERED_POSITIONS = MineColoniesCompatibility.rl("registered_positions").toString();

	private final Set<BlockPos> workingPositions = new HashSet<>();
	private WorkingBlocksPathResult pathReuslt;

	public AbstractModuleWithExternalWorkingBlocks()
	{

	}

	@Override
	public void deserializeNBT(CompoundTag compound)
	{
		this.workingPositions.clear();
		this.workingPositions.addAll(BlockPosUtil.readPosListFromNBT(compound, TAG_REGISTERED_POSITIONS));
	}

	@Override
	public void serializeNBT(@NotNull CompoundTag compound)
	{
		BlockPosUtil.writePosListToNBT(compound, TAG_REGISTERED_POSITIONS, new ArrayList<>(this.workingPositions));
	}

	@Override
	public boolean requestFindWorkingBlocks()
	{
		var worker = this.getPathFindingCitizen();
		return this.requestFindWorkingBlocks(worker);
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
			for (var pos : this.pathReuslt.positions)
			{
				if (this.workingPositions.add(pos))
				{
					this.onWorkingBlockAdded(pos);
				}

			}

			this.pathReuslt = null;
			this.markDirty();
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
		this.requestFindWorkingBlocks();
	}

	@Nullable
	protected AbstractEntityCitizen getPathFindingCitizen()
	{
		return null;
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

		if (this.isWorkingBlock(level, pos, state) && this.workingPositions.add(pos))
		{
			this.onWorkingBlockAdded(pos);
			this.markDirty();
			return true;
		}

		return false;
	}

	protected void onWorkingBlockAdded(@NotNull BlockPos pos)
	{

	}

	@Override
	public boolean removeWorkingBlock(@Nullable BlockPos pos)
	{
		if (this.workingPositions.remove(pos))
		{
			this.onWorkingBlockRemoved(pos);
			this.markDirty();
			return true;
		}

		return false;
	}

	protected void onWorkingBlockRemoved(@Nullable BlockPos pos)
	{

	}

	@Override
	public boolean containsWorkingBlock(@Nullable BlockPos pos)
	{
		return this.workingPositions.contains(pos);
	}

	@Override
	public List<BlockPos> getRegisteredBlocks()
	{
		return new ArrayList<>(this.workingPositions);
	}

}
