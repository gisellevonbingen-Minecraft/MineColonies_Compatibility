package steve_gall.minecolonies_compatibility.module.common.delightful;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.brnbrd.delightful.common.block.DelightfulBlocks;
import net.brnbrd.delightful.common.block.SalmonberryBushBlock;
import net.brnbrd.delightful.common.item.DelightfulItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantSeedContext;

public class SalmonberryCrop extends CustomizedCrop
{
	@Override
	public boolean isSeed(@NotNull PlantSeedContext context)
	{
		return context.getSeed().is(DelightfulItems.SALMONBERRY_PIPS.get());
	}

	@Override
	public boolean isCrop(@NotNull PlantBlockContext context)
	{
		return context.getState().is(DelightfulBlocks.SALMONBERRY_BUSH.get());
	}

	@Override
	public @Nullable SpecialHarvestPositionFunction getSpecialHarvestPosition(@NotNull PlantBlockContext context)
	{
		return this::getHarvestPosition;
	}

	@Override
	public @Nullable SpecialHarvestMethodFunction getSpecialHarvestMethod(@NotNull PlantBlockContext context)
	{
		return this::harvest;
	}

	private @Nullable BlockPos getHarvestPosition(@NotNull PlantBlockContext context)
	{
		var state = context.getState();

		if (state.getBlock() instanceof SalmonberryBushBlock block && block.isMaxAge(state))
		{
			return context.getPosition();
		}
		else
		{
			return null;
		}

	}

	private @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof ServerLevel level)
		{
			var state = context.getState();
			var flag = ((SalmonberryBushBlock) state.getBlock()).isMaxAge(state);
			var count = flag ? (2 + level.random.nextInt(2)) : 1;
			level.setBlock(context.getPosition(), state.setValue(SalmonberryBushBlock.AGE, 1), Block.UPDATE_CLIENTS);

			return Collections.singletonList(new ItemStack(DelightfulItems.SALMONBERRIES.get(), count));
		}
		else
		{
			return Collections.emptyList();
		}

	}

}
