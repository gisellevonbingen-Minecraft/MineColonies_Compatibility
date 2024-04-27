package steve_gall.minecolonies_compatibility.core.common.module.ie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import blusunrize.immersiveengineering.common.blocks.plant.EnumHempGrowth;
import blusunrize.immersiveengineering.common.blocks.plant.HempBlock;
import blusunrize.immersiveengineering.common.items.IESeedItem;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.register.IEItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedCrop;

public class HempCrop extends CustomizedCrop
{
	@Override
	public boolean isSeed(@NotNull ItemStack seedStack)
	{
		return seedStack.getItem() == IEItems.Misc.HEMP_SEEDS.get();
	}

	@Override
	public boolean isCrop(@NotNull BlockState cropState)
	{
		return cropState.getBlock() == IEBlocks.Misc.HEMP_PLANT.get();
	}

	@Override
	@Nullable
	public BlockState getPlantState(@NotNull ItemStack seedStack, @NotNull Level level, @NotNull BlockPos plantPosition)
	{
		return ((IESeedItem) seedStack.getItem()).getPlant(level, plantPosition);
	}

	@Override
	public boolean hasSpecialHarvestPosition(@NotNull BlockState cropState, @NotNull Level level, @NotNull BlockPos plantPosition)
	{
		return true;
	}

	@Nullable
	@Override
	public BlockPos getSpecialHarvestPosition(@NotNull BlockState cropState, @NotNull Level level, @NotNull BlockPos plantPosition)
	{
		var topPos = plantPosition.above();
		var topState = level.getBlockState(topPos);

		if (topState.getBlock() instanceof HempBlock && topState.getValue(HempBlock.GROWTH) == EnumHempGrowth.TOP0)
		{
			return topPos;
		}
		else
		{
			return null;
		}

	}

}
