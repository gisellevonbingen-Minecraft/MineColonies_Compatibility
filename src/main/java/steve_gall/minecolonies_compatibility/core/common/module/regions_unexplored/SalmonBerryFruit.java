package steve_gall.minecolonies_compatibility.core.common.module.regions_unexplored;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.regions_unexplored.item.RuItems;
import net.regions_unexplored.world.level.block.plant.food.SalmonBerryBushBlock;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.PlantBlockContext;

public class SalmonBerryFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof SalmonBerryBushBlock;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(SalmonBerryBushBlock.AGE) > 1;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context)
	{
		var state = context.getState();
		var age = state.getValue(SalmonBerryBushBlock.AGE);
		var flag = age == SalmonBerryBushBlock.MAX_AGE;

		if (context.getLevel() instanceof LevelWriter level)
		{
			level.setBlock(context.getPosition(), state.setValue(SalmonBerryBushBlock.AGE, 1), Block.UPDATE_CLIENTS);
		}

		return Collections.singletonList(new ItemStack(RuItems.SALMONBERRY.get(), flag ? 2 : 1));
	}

}
