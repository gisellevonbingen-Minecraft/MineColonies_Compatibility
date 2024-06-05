package steve_gall.minecolonies_compatibility.api.common.entity.pathfinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import steve_gall.minecolonies_tweaks.api.common.pathfinding.SimplePathJob;

public class PathJobFindWorkingBlocks<RESULT extends WorkingBlocksPathResult> extends SimplePathJob<RESULT>
{
	private final BlockPos.MutableBlockPos tempPos = new BlockPos.MutableBlockPos();

	public PathJobFindWorkingBlocks(@NotNull Level level, @NotNull BlockPos start, @NotNull BlockPos home, int range, @Nullable Mob entity, @NotNull RESULT result)
	{
		super(level, start, home, range, entity, result);
	}

	public PathJobFindWorkingBlocks(@NotNull Level level, @NotNull BlockPos start, @NotNull BoundingBox restrictionBox, @Nullable Mob entity, @NotNull RESULT result)
	{
		super(level, start, restrictionBox, entity, result);
	}

	@NotNull
	public LevelReader getLevel()
	{
		return this.world;
	}

	@Override
	protected double computeHeuristic(int x, int y, int z)
	{
		return 0.0D;
	}

	@Override
	protected boolean testTarget(int x, int y, int z)
	{
		this.getResult().test(this, this.tempPos.set(x, y, z));
		return false;
	}

}
