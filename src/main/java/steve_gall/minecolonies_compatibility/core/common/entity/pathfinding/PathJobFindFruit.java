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
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.Fruit;
import steve_gall.minecolonies_tweaks.api.common.pathfinding.SimplePathJob;

public class PathJobFindFruit extends SimplePathJob<FruitPathResult>
{
	public int vertialRange = 10;
	public boolean canBoneMeal = true;
	public boolean needMaxHarvest = true;
	public final Set<ResourceLocation> exceptFruits = new HashSet<>();

	public PathJobFindFruit(@NotNull Level level, @NotNull BlockPos start, @NotNull BlockPos home, int range, @Nullable Mob entity)
	{
		super(level, start, home, range, entity, new FruitPathResult());
	}

	public PathJobFindFruit(@NotNull Level level, @NotNull BlockPos start, @NotNull BoundingBox restrictionBox, @Nullable Mob entity)
	{
		super(level, start, restrictionBox, entity, new FruitPathResult());
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
		var fruit = new Fruit(pos.immutable());

		if (this.testFruit(fruit))
		{
			this.getResult().fruit = fruit;
			return true;
		}

		return false;
	}

	private boolean testFruit(Fruit fruit)
	{
		if (!fruit.updateAndIsValid(this.world))
		{
			return false;
		}
		else if (this.exceptFruits.contains(fruit.getFruit().getId()))
		{
			return false;
		}
		else if (fruit.canHarvest(this.needMaxHarvest))
		{
			return true;
		}
		else
		{
			return this.canBoneMeal && fruit.getContext().getState().getBlock() instanceof BonemealableBlock;
		}

	}

}
