package steve_gall.minecolonies_compatibility.module.common.regions_unexplored;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.regions_unexplored.registry.RUBlocks;
import net.regions_unexplored.registry.RUItems;
import net.regions_unexplored.world.level.block.plant.food.SalmonBerryBushBlock;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class SalmonBerryFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return BuiltInRegistries.BLOCK.getKey(RUBlocks.SALMONBERRY_BUSH.get());
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Arrays.asList(new ItemStack(RUItems.SALMONBERRY.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Arrays.asList(new ItemStack(RUItems.SALMONBERRY.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == RUBlocks.SALMONBERRY_BUSH.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(SalmonBerryBushBlock.AGE) > 1;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(SalmonBerryBushBlock.AGE) == SalmonBerryBushBlock.MAX_AGE;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		var state = context.getState();
		var age = state.getValue(SalmonBerryBushBlock.AGE);
		var flag = age == SalmonBerryBushBlock.MAX_AGE;

		if (context.getLevel() instanceof LevelWriter level)
		{
			level.setBlock(context.getPosition(), state.setValue(SalmonBerryBushBlock.AGE, 1), Block.UPDATE_CLIENTS);
		}

		return Collections.singletonList(new ItemStack(RUItems.SALMONBERRY.get(), flag ? 2 : 1));
	}

}
