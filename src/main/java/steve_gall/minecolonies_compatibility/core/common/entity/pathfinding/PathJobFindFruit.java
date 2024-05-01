package steve_gall.minecolonies_compatibility.core.common.entity.pathfinding;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.pathfinding.AbstractAdvancedPathNavigate;
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.core.entity.pathfinding.MNode;
import com.minecolonies.core.entity.pathfinding.pathjobs.AbstractPathJob;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.Fruit;

public class PathJobFindFruit extends AbstractPathJob
{
	public static final double TIE_BREAKER = 0.951D;
	public static final Vec3i AREA_SHRINK = new Vec3i(-1, 0, -1);

	private final BlockPos hutLocation;
	private final BlockPos boxCenter;

	public int vertialRange = 10;
	public boolean needHarvestable = true;

	public PathJobFindFruit(Level world, @NotNull BlockPos start, BlockPos home, int range, Mob entity)
	{
		super(world, start, start, range, new FruitPathResult(), entity);
		this.hutLocation = home;

		this.boxCenter = null;
	}

	public PathJobFindFruit(Level world, @NotNull BlockPos start, BlockPos home, BlockPos startRestriction, BlockPos endRestriction, BlockPos furthestRestriction, Mob entity)
	{
		super(world, start, startRestriction, endRestriction, (int) Math.sqrt(BlockPosUtil.getDistanceSquared2D(home, furthestRestriction) * 1.5D), AREA_SHRINK, false, new FruitPathResult(), entity, AbstractAdvancedPathNavigate.RestrictionType.XZ);
		this.hutLocation = home;

		var size = startRestriction.subtract(endRestriction);
		this.boxCenter = endRestriction.offset(size.getX() / 2, size.getY() / 2, size.getZ() / 2);
	}

	@NotNull
	@Override
	public FruitPathResult getResult()
	{
		return (FruitPathResult) super.getResult();
	}

	@Override
	protected double computeHeuristic(@NotNull BlockPos pos)
	{
		return this.boxCenter == null ? pos.distSqr(this.hutLocation) * TIE_BREAKER : BlockPosUtil.getDistanceSquared2D(pos, this.boxCenter);
	}

	@Override
	protected boolean isAtDestination(@NotNull MNode n)
	{
		return n.parent != null && this.findFruitAtNode(n);
	}

	private boolean findFruitAtNode(@NotNull MNode n)
	{
		var pPos = n.parent.pos;
		var nx = n.pos.getX();
		var ny = n.pos.getY();
		var nz = n.pos.getZ();

		if (nx == pPos.getX())
		{
			var dz = nz > pPos.getZ() ? 1 : -1;
			return this.findFruitAtXZ(nx, ny, nz + dz) || this.findFruitAtXZ(nx - 1, ny, nz) || this.findFruitAtXZ(nx + 1, ny, nz);
		}
		else
		{
			var dx = nx > pPos.getX() ? 1 : -1;
			return this.findFruitAtXZ(nx + dx, ny, nz) || this.findFruitAtXZ(nx, ny, nz - 1) || this.findFruitAtXZ(nx, ny, nz + 1);
		}

	}

	private boolean findFruitAtXZ(int x, int y, int z)
	{
		for (var i = 0; i <= this.vertialRange; i++)
		{
			if (this.findFruitAt(x, y + i, z))
			{
				return true;
			}

		}

		return false;
	}

	private boolean findFruitAt(int x, int y, int z)
	{
		var fruit = new Fruit(new BlockPos(x, y, z));

		if (this.test(fruit))
		{
			this.getResult().fruit = fruit;
			return true;
		}

		return false;
	}

	private boolean test(Fruit fruit)
	{
		if (!fruit.updateAndIsValid(this.world))
		{
			return false;
		}

		if (this.needHarvestable)
		{
			if (!fruit.canHarvest())
			{
				return false;
			}

		}
		else if (!(fruit.getContext().getState().getBlock() instanceof BonemealableBlock))
		{
			return false;
		}

		return true;
	}

	@Override
	protected double getNodeResultScore(MNode n)
	{
		return 0;
	}

}
