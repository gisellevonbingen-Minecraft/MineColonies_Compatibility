package steve_gall.minecolonies_compatibility.module.common.culturaldelights;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.block.custom.CornUpperBlock;
import com.baisylia.culturaldelights.item.ModItems;

import net.minecraft.core.BlockPos;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantSeedContext;

public class CornCrop extends CustomizedCrop
{
	@Override
	public boolean isSeed(@NotNull PlantSeedContext context)
	{
		return context.getSeed().is(ModItems.CORN_KERNELS.get());
	}

	@Override
	public boolean isCrop(@NotNull PlantBlockContext context)
	{
		return context.getState().is(ModBlocks.CORN.get());
	}

	@Override
	public @Nullable SpecialHarvestPositionFunction getSpecialHarvestPosition(@NotNull PlantBlockContext context)
	{
		return this::getHarvestPosition;
	}

	private @Nullable BlockPos getHarvestPosition(@NotNull PlantBlockContext context)
	{
		var upperState = context.getLevel().getBlockState(context.getPosition().above());

		if (upperState.getBlock() instanceof CornUpperBlock upper && upper.isMaxAge(upperState))
		{
			return context.getPosition().above();
		}
		else
		{
			return null;
		}

	}

}
