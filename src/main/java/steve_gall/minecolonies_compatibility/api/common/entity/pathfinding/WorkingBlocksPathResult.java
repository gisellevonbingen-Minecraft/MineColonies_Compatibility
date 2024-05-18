package steve_gall.minecolonies_compatibility.api.common.entity.pathfinding;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.core.entity.pathfinding.pathresults.PathResult;

import net.minecraft.core.BlockPos;
import steve_gall.minecolonies_compatibility.api.common.building.module.IModuleWithExternalWorkingBlocks;

@SuppressWarnings("rawtypes")
public abstract class WorkingBlocksPathResult extends PathResult
{
	public final Set<BlockPos> positions = new HashSet<>();

	@NotNull
	private final IModuleWithExternalWorkingBlocks module;

	public WorkingBlocksPathResult(@NotNull IModuleWithExternalWorkingBlocks module)
	{
		this.module = module;
	}

	@NotNull
	public IModuleWithExternalWorkingBlocks getModule()
	{
		return this.module;
	}

	public boolean test(@NotNull PathJobFindWorkingBlocks<?> job, @NotNull BlockPos.MutableBlockPos pos)
	{
		var level = job.getLevel();
		var state = level.getBlockState(pos);

		if (this.getModule().isWorkingBlock(level, pos, state))
		{
			this.positions.add(pos.immutable());
			return true;
		}
		else
		{
			return false;
		}

	}

}
