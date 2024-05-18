package steve_gall.minecolonies_compatibility.core.common.entity.pathfinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import steve_gall.minecolonies_tweaks.api.common.pathfinding.SimplePathJob;

public class PathJobMatchedBlocks extends SimplePathJob<MatchBlocksPathResult>
{
	@NotNull
	private final BlockPos.MutableBlockPos tempPos = new BlockPos.MutableBlockPos();
	@NotNull
	private final MatchPredicate predicate;

	public PathJobMatchedBlocks(@NotNull Level level, @NotNull BlockPos start, @NotNull BlockPos home, int range, @Nullable Mob entity, @NotNull MatchPredicate predicate)
	{
		super(level, start, home, range, entity, new MatchBlocksPathResult());
		this.predicate = predicate;
	}

	public PathJobMatchedBlocks(@NotNull Level level, @NotNull BlockPos start, @NotNull BoundingBox restrictionBox, @Nullable Mob entity, @NotNull MatchPredicate predicate)
	{
		super(level, start, restrictionBox, entity, new MatchBlocksPathResult());
		this.predicate = predicate;
	}

	@Override
	protected boolean testTarget(int x, int y, int z)
	{
		if (this.predicate.match(this.world, this.tempPos.set(x, y, z)))
		{
			this.getResult().positions.add(this.tempPos.immutable());
			return true;
		}
		else
		{
			return false;
		}

	}

	@FunctionalInterface
	public static interface MatchPredicate
	{
		boolean match(@NotNull LevelReader level, @NotNull BlockPos.MutableBlockPos position);
	}

}
