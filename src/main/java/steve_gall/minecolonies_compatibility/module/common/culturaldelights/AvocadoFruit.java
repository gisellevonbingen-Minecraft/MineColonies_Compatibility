package steve_gall.minecolonies_compatibility.module.common.culturaldelights;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.block.custom.FruitingLeaves;
import com.baisylia.culturaldelights.item.ModItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class AvocadoFruit extends CustomizedFruit
{
	public AvocadoFruit()
	{

	}

	@Override
	public @NotNull ResourceLocation getId()
	{
		return ModBlocks.FRUITING_AVOCADO_LEAVES.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Arrays.asList(new ItemStack(ModBlocks.FRUITING_AVOCADO_LEAVES.get()), new ItemStack(ModBlocks.AVOCADO_SAPLING.get()), new ItemStack(ModBlocks.AVOCADO_PIT.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Arrays.asList(new ItemStack(ModItems.AVOCADO.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().is(ModBlocks.FRUITING_AVOCADO_LEAVES.get());
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(FruitingLeaves.AGE) == FruitingLeaves.MAX_AGE;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof ServerLevel level)
		{
			var j = 2 + level.random.nextInt(2);
			level.setBlock(context.getPosition(), context.getState().setValue(FruitingLeaves.AGE, 0), Block.UPDATE_CLIENTS);
			return Collections.singletonList(new ItemStack(ModItems.AVOCADO.get(), j));
		}

		return Collections.emptyList();
	}

}
