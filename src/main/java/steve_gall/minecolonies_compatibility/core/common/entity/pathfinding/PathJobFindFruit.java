package steve_gall.minecolonies_compatibility.core.common.entity.pathfinding;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.core.entity.pathfinding.MNode;
import com.minecolonies.core.entity.pathfinding.SurfaceType;
import com.minecolonies.core.entity.pathfinding.pathjobs.AbstractPathJob;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.phys.AABB;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.Fruit;

public class PathJobFindFruit extends AbstractPathJob
{
	private final BlockPos searchTowards;
	private AABB restrictionBox = null;

	public int vertialRange = 10;
	public boolean needHarvestable = true;

	public PathJobFindFruit(Level world, @NotNull BlockPos start, BlockPos home, int range, Mob entity)
	{
		super(world, start, range, new FruitPathResult(), entity);
		this.searchTowards = home;
	}

	public PathJobFindFruit(Level world, @NotNull BlockPos start, BlockPos startRestriction, BlockPos endRestriction, BlockPos furthestRestriction, Mob entity)
	{
		super(world, start, (int) getRange(startRestriction, endRestriction, entity), new FruitPathResult(), entity);
		this.restrictionBox = new AABB(//
				Math.min(startRestriction.getX(), endRestriction.getX()), //
				Math.min(startRestriction.getY(), endRestriction.getY()), //
				Math.min(startRestriction.getZ(), endRestriction.getZ()), //
				Math.max(startRestriction.getX(), endRestriction.getX()), //
				Math.max(startRestriction.getY(), endRestriction.getY()), //
				Math.max(startRestriction.getZ(), endRestriction.getZ())//
		);
		this.searchTowards = BlockPos.containing(this.restrictionBox.getCenter());
	}

	private static double getRange(BlockPos startRestriction, BlockPos endRestriction, Mob entity)
	{
		var x2 = (startRestriction.getX() + endRestriction.getX()) / 2;
		var y2 = (startRestriction.getY() + endRestriction.getY()) / 2;
		var z2 = (startRestriction.getZ() + endRestriction.getZ()) / 2;
		return BlockPosUtil.dist(entity.blockPosition(), x2, y2, z2) + BlockPosUtil.dist(startRestriction, endRestriction);
	}

	@NotNull
	@Override
	public FruitPathResult getResult()
	{
		return (FruitPathResult) super.getResult();
	}

	@Override
	protected double computeHeuristic(int x, int y, int z)
	{
		return this.searchTowards == null ? BlockPosUtil.distManhattan(this.start, x, y, z) : BlockPosUtil.distManhattan(this.searchTowards, x, y, z);
	}

	@Override
	protected boolean isAtDestination(@NotNull MNode n)
	{
		if (this.restrictionBox != null && !this.restrictionBox.contains(n.x, n.y, n.z))
		{
			return false;
		}

		return n.parent != null && this.findFruitAtNode(n) && SurfaceType.getSurfaceType(this.world, this.cachedBlockLookup.getBlockState(n.x, n.y - 1, n.z), this.tempWorldPos.set(n.x, n.y - 1, n.z), this.getPathingOptions()) == SurfaceType.WALKABLE;
	}

	private boolean findFruitAtNode(@NotNull MNode n)
	{
		if (n.parent == null)
		{
			return false;
		}

		if (n.x == n.parent.x)
		{
			var dz = n.z > n.parent.z ? 1 : -1;
			return this.findFruitAtXZ(n.x, n.y, n.z + dz) || this.findFruitAtXZ(n.x - 1, n.y, n.z) || this.findFruitAtXZ(n.x + 1, n.y, n.z);
		}
		else
		{
			var dx = n.x > n.parent.x ? 1 : -1;
			return this.findFruitAtXZ(n.x + dx, n.y, n.z) || this.findFruitAtXZ(n.x, n.y, n.z - 1) || this.findFruitAtXZ(n.x, n.y, n.z + 1);
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
		if (this.restrictionBox != null && !this.restrictionBox.contains(x, y, z))
		{
			return false;
		}

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
	protected double getEndNodeScore(MNode n)
	{
		return BlockPosUtil.distManhattan(this.searchTowards, n.x, n.y, n.z);
	}

}
