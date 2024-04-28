package steve_gall.minecolonies_compatibility.core.common.module.farmersdelight;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedCrop;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

public class BuddingTomatoCrop extends CustomizedCrop
{
	@Override
	public boolean isSeed(@NotNull ItemStack seedStack)
	{
		return seedStack.getItem() == ModItems.TOMATO_SEEDS.get();
	}

	@Override
	public boolean isCrop(@NotNull BlockState cropState)
	{
		return cropState.getBlock() == ModBlocks.BUDDING_TOMATO_CROP.get();
	}

	@Override
	public @Nullable BlockState getPlantState(@NotNull ItemStack seedStack, @NotNull Level level, @NotNull BlockPos plantPosition)
	{
		return ((BlockItem) seedStack.getItem()).getBlock().defaultBlockState();
	}

}
