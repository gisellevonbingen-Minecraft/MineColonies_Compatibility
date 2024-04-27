package steve_gall.minecolonies_compatibility.core.common.module.farmersdelight;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;
import vectorwing.farmersdelight.common.block.TomatoVineBlock;
import vectorwing.farmersdelight.common.registry.ModItems;

public class TomatoVineFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull BlockState state)
	{
		return state.getBlock() instanceof TomatoVineBlock;
	}

	@Override
	public boolean canHarvest(@NotNull BlockState state)
	{
		return state.getBlock() instanceof TomatoVineBlock block && block.isMaxAge(state);
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos position)
	{
		level.setBlock(position, state.setValue(((TomatoVineBlock) state.getBlock()).getAgeProperty(), 0), Block.UPDATE_CLIENTS);

		var list = new ArrayList<ItemStack>();
		list.add(new ItemStack(ModItems.TOMATO.get(), 1 + level.random.nextInt(2)));

		if (level.random.nextFloat() < 0.05D)
		{
			list.add(new ItemStack(ModItems.ROTTEN_TOMATO.get()));
		}

		return list;
	}

}
