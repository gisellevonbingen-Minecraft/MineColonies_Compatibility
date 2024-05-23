package steve_gall.minecolonies_compatibility.module.common.minecraft;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class SweetBerryFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return ForgeRegistries.BLOCKS.getKey(Blocks.SWEET_BERRY_BUSH);
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Arrays.asList(new ItemStack(Items.SWEET_BERRIES));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Arrays.asList(new ItemStack(Items.SWEET_BERRIES));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == Blocks.SWEET_BERRY_BUSH;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(SweetBerryBushBlock.AGE) > 1;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(SweetBerryBushBlock.AGE) == SweetBerryBushBlock.MAX_AGE;
	}

	@Override
	@NotNull
	public List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof ServerLevel level)
		{
			var state = context.getState();
			var age = state.getValue(SweetBerryBushBlock.AGE);
			var count = 1 + level.random.nextInt(2) + (age == SweetBerryBushBlock.MAX_AGE ? 1 : 0);
			level.setBlock(context.getPosition(), state.setValue(SweetBerryBushBlock.AGE, 1), Block.UPDATE_CLIENTS);

			return Collections.singletonList(new ItemStack(Items.SWEET_BERRIES, count));
		}
		else
		{
			return Collections.emptyList();
		}

	}

}
