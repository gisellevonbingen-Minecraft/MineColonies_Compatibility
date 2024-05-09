package steve_gall.minecolonies_compatibility.core.common.module.undergarden;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import quek.undergarden.block.UnderbeanBushBlock;
import quek.undergarden.registry.UGItems;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class UnderbeanFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof UnderbeanBushBlock;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(UnderbeanBushBlock.AGE) > 1;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(UnderbeanBushBlock.AGE) == 3;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof ServerLevel level)
		{
			var state = context.getState();
			var i = state.getValue(UnderbeanBushBlock.AGE);
			var flag = i == 3;
			var j = 1 + level.random.nextInt(2);

			var newState = state.setValue(UnderbeanBushBlock.AGE, 1);
			level.setBlock(context.getPosition(), newState, Block.UPDATE_CLIENTS);

			return Collections.singletonList(new ItemStack(UGItems.UNDERBEANS.get(), j + (flag ? 1 : 0)));
		}

		return Collections.emptyList();
	}

}
