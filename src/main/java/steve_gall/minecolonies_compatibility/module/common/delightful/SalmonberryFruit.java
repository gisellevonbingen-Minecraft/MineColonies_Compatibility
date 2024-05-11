package steve_gall.minecolonies_compatibility.module.common.delightful;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.brnbrd.delightful.common.block.DelightfulBlocks;
import net.brnbrd.delightful.common.block.SalmonberryBushBlock;
import net.brnbrd.delightful.common.item.DelightfulItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class SalmonberryFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == DelightfulBlocks.SALMONBERRY_BUSH.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(SalmonberryBushBlock.AGE) > 2;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		var state = context.getState();
		return ((SalmonberryBushBlock) state.getBlock()).isMaxAge(state);
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
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
