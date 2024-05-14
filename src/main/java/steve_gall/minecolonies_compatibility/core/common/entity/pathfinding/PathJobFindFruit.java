package steve_gall.minecolonies_compatibility.core.common.entity.pathfinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.Fruit;
import steve_gall.minecolonies_tweaks.api.common.pathfinding.SimplePathJob;

public class PathJobFindFruit extends SimplePathJob<FruitPathResult>
{
	public int vertialRange = 10;
	public boolean needHarvestable = true;
	public boolean needMaxHarvest = true;

	public PathJobFindFruit(@NotNull Level level, @NotNull BlockPos start, @NotNull BlockPos home, int range, @Nullable Mob entity)
	{
		super(level, start, home, range, entity, new FruitPathResult());
	}

	public PathJobFindFruit(@NotNull Level level, @NotNull BlockPos start, @NotNull BoundingBox restrictionBox, @Nullable Mob entity)
	{
		super(level, start, restrictionBox, entity, new FruitPathResult());
	}

	@Override
	protected boolean testTarget(int x, int y, int z)
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
		else if (this.needHarvestable)
		{
			return fruit.canHarvest(this.needMaxHarvest);
		}
		else
		{
			return fruit.getContext().getState().getBlock() instanceof BonemealableBlock;
		}

	}

}
