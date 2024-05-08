package steve_gall.minecolonies_compatibility.core.common.module.ie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import blusunrize.immersiveengineering.common.blocks.plant.EnumHempGrowth;
import blusunrize.immersiveengineering.common.blocks.plant.HempBlock;
import blusunrize.immersiveengineering.common.items.IESeedItem;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.register.IEItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantSeedContext;

public class HempCrop extends CustomizedCrop
{
	@Override
	public boolean isSeed(@NotNull PlantSeedContext context)
	{
		return context.getSeed().getItem() == IEItems.Misc.HEMP_SEEDS.get();
	}

	@Override
	public boolean isCrop(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == IEBlocks.Misc.HEMP_PLANT.get();
	}

	@Override
	@Nullable
	public BlockState getPlantState(@NotNull PlantSeedContext context)
	{
		return ((IESeedItem) context.getSeed().getItem()).getPlant(context.getLevel(), context.getPosition());
	}

	@Override
	@Nullable
	public SpecialHarvestPositionFunction getSpecialHarvestPosition(@NotNull PlantBlockContext context)
	{
		return this::getHarvestPosition;
	}

	@Nullable
	private BlockPos getHarvestPosition(@NotNull PlantBlockContext context)
	{
		var topPos = context.getPosition().above();
		var topState = context.getLevel().getBlockState(topPos);

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
