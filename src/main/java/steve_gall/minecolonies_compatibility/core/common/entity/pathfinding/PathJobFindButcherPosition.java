package steve_gall.minecolonies_compatibility.core.common.entity.pathfinding;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherBlockContext;
import steve_gall.minecolonies_compatibility.api.common.butcher.CustomizedButcherable;
import steve_gall.minecolonies_tweaks.api.common.pathfinding.SimplePathJob;

public class PathJobFindButcherPosition extends SimplePathJob<ButcherPositionsPathResult>
{
	public int vertialRange = 0;
	public final Set<ResourceLocation> exceptButcherables = new HashSet<>();
	public final Set<BlockPos> positions = new HashSet<>();
	public final Set<CustomizedButcherable> blocks = new HashSet<>();
	public final Set<CustomizedButcherable> tables = new HashSet<>();

	public PathJobFindButcherPosition(@NotNull Level level, @NotNull BlockPos start, @NotNull BlockPos home, int range, @Nullable Mob entity)
	{
		super(level, start, home, range, entity, new ButcherPositionsPathResult());
	}

	public PathJobFindButcherPosition(@NotNull Level level, @NotNull BlockPos start, @NotNull BoundingBox restrictionBox, @Nullable Mob entity)
	{
		super(level, start, restrictionBox, entity, new ButcherPositionsPathResult());
	}

	@Override
	protected boolean isTarget(@NotNull MutableBlockPos pos)
	{
		var y = pos.getY();

		for (var i = 0; i <= this.vertialRange; i++)
		{
			if (super.isTarget(pos.setY(y + i)))
			{
				return true;
			}

		}

		return false;
	}

	@Override
	protected boolean testPos(@NotNull MutableBlockPos pos)
	{
		var context = new ButcherBlockContext(this.world, pos, this.world.getBlockState(pos));

		for (var butcherable : CustomizedButcherable.getRegistry().values())
		{
			if (this.exceptButcherables.contains(butcherable.getId()))
			{
				continue;
			}
			else if (this.positions.contains(pos))
			{
				continue;
			}
			else if (butcherable.isButcheringBlock(context))
			{
				if (this.blocks.contains(butcherable))
				{
					continue;
				}

				this.getResult().blocks.add(pos.immutable());
				this.blocks.add(butcherable);
				this.positions.add(pos.immutable());
				break;
			}
			else if (butcherable.isTableBlock(context))
			{
				if (this.tables.contains(butcherable))
				{
					continue;
				}

				this.getResult().tables.add(pos.immutable());
				this.tables.add(butcherable);
				this.positions.add(pos.immutable());
				break;
			}

		}

		return false;
	}

}
