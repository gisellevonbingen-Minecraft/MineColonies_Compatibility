package steve_gall.minecolonies_compatibility.core.common.module.minecraft;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;

public class SweetBerryFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull BlockState state)
	{
		return state.getBlock() instanceof SweetBerryBushBlock;
	}

	@Override
	public boolean canHarvest(@NotNull BlockState state)
	{
		return state.getBlock() instanceof SweetBerryBushBlock block && state.getValue(SweetBerryBushBlock.AGE) > 1;
	}

	@Override
	@NotNull
	public List<ItemStack> harvest(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos position)
	{
		var age = state.getValue(SweetBerryBushBlock.AGE);
		var count = 1 + level.random.nextInt(2) + (age == SweetBerryBushBlock.MAX_AGE ? 1 : 0);
		level.setBlock(position, state.setValue(SweetBerryBushBlock.AGE, 1), Block.UPDATE_CLIENTS);

		return Collections.singletonList(new ItemStack(Items.SWEET_BERRIES, count));
	}

}
