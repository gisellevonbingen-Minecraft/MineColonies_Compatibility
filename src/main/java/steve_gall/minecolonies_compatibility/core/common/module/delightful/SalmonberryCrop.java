package steve_gall.minecolonies_compatibility.core.common.module.delightful;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.brnbrd.delightful.common.block.DelightfulBlocks;
import net.brnbrd.delightful.common.block.SalmonberryBushBlock;
import net.brnbrd.delightful.common.item.DelightfulItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.PlantSeedContext;

public class SalmonberryCrop extends CustomizedCrop
{
	@Override
	public boolean isSeed(@NotNull PlantSeedContext context)
	{
		return context.getSeed().getItem() == DelightfulItems.SALMONBERRY_PIPS.get();
	}

	@Override
	public @Nullable BlockState getPlantState(@NotNull PlantSeedContext context)
	{
		return ((IPlantable) ((BlockItem) context.getSeed().getItem()).getBlock()).getPlant(context.getLevel(), context.getPosition());
	}

	@Override
	public boolean isCrop(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == DelightfulBlocks.SALMONBERRY_BUSH.get();
	}

	@Override
	public @Nullable SpecialHarvestPositionFunction getSpecialHarvestPosition(@NotNull PlantBlockContext context)
	{
		return new SpecialHarvestPositionFunction()
		{
			@Override
			public @Nullable BlockPos apply(@NotNull PlantBlockContext context)
			{
				return canHarvest(context) ? context.getPosition() : null;
			}

		};
	}

	@Override
	public @Nullable SpecialHarvestMethodFunction getSpecialHarvestMethod(@NotNull PlantBlockContext context)
	{
		return SalmonberryCrop::harvest;
	}

	public static boolean canHarvest(PlantBlockContext context)
	{
		return context.getState().getValue(SalmonberryBushBlock.AGE) > 2;
	}

	public static List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		var state = context.getState();

		if (context.getLevel() instanceof ServerLevel level)
		{
			var flag = ((SalmonberryBushBlock) state.getBlock()).isMaxAge(state);
			var count = flag ? 2 + level.random.nextInt(2) : 1;
			level.setBlock(context.getPosition(), state.setValue(SalmonberryBushBlock.AGE, 1), Block.UPDATE_CLIENTS);

			return Collections.singletonList(new ItemStack(DelightfulItems.SALMONBERRIES.get(), count));
		}
		else
		{
			return Collections.emptyList();
		}

	}

}
