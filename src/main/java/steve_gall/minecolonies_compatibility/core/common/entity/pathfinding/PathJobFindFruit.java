package steve_gall.minecolonies_compatibility.core.common.entity.pathfinding;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.pathfinding.AbstractAdvancedPathNavigate;
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.core.entity.pathfinding.MNode;
import com.minecolonies.core.entity.pathfinding.pathjobs.AbstractPathJob;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.Fruit;

public class PathJobFindFruit extends AbstractPathJob
{
	public static final double TIE_BREAKER = 0.951D;
	public static final Vec3i AREA_SHRINK = new Vec3i(-1, 0, -1);

	private final BlockPos hutLocation;
	private final BlockPos boxCenter;

	public int vertialRange = 10;
	public boolean needHarvestable = true;

	public PathJobFindFruit(Level world, @NotNull BlockPos start, BlockPos home, int range, LivingEntity entity)
	{
		super(world, start, start, range, new FruitPathResult(), entity);
		this.hutLocation = home;

		this.boxCenter = null;
	}

	public PathJobFindFruit(Level world, @NotNull BlockPos start, BlockPos home, BlockPos startRestriction, BlockPos endRestriction, BlockPos furthestRestriction, LivingEntity entity)
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
		var nPos = n.pos;

		if (nPos.getX() == pPos.getX())
		{
			var dz = nPos.getZ() > pPos.getZ() ? 1 : -1;
			return this.findFruitAtXZ(nPos.offset(0, 0, dz)) || this.findFruitAtXZ(nPos.offset(-1, 0, 0)) || this.findFruitAtXZ(nPos.offset(1, 0, 0));
		}
		else
		{
			var dx = nPos.getX() > pPos.getX() ? 1 : -1;
			return this.findFruitAtXZ(nPos.offset(dx, 0, 0)) || this.findFruitAtXZ(nPos.offset(0, 0, -1)) || this.findFruitAtXZ(nPos.offset(0, 0, 1));
		}

	}

	private boolean findFruitAtXZ(@NotNull BlockPos pos)
	{
		for (var i = 0; i <= this.vertialRange; i++)
		{
			if (this.findFruitAt(pos.offset(0, i, 0)))
			{
				return true;
			}

		}

		return false;
	}

	private boolean findFruitAt(@NotNull BlockPos pos)
	{
		var fruit = new Fruit(pos);

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
		else if (!(fruit.getState().getBlock() instanceof BonemealableBlock))
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

	@Override
	protected boolean isPassable(@NotNull BlockState block, BlockPos pos, MNode parent, boolean head)
	{
		return super.isPassable(block, pos, parent, head) || (block.is(BlockTags.LEAVES) && this.isInRestrictedArea(pos));
	}

}
