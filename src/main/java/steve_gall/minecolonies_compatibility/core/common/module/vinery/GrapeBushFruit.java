package steve_gall.minecolonies_compatibility.core.common.module.vinery;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import satisfyu.vinery.block.grape.GrapeBush;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class GrapeBushFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof GrapeBush;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(GrapeBush.AGE) > 1;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(GrapeBush.AGE) == 3;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof ServerLevel level)
		{
			var state = context.getState();
			var newState = state.setValue(GrapeBush.AGE, 1);
			level.setBlock(context.getPosition(), newState, Block.UPDATE_CLIENTS);

			var i = state.getValue(GrapeBush.AGE);
			var bl = i == 3;
			var x = level.random.nextInt(2);
			var item = ((GrapeBush) state.getBlock()).getGrapeType().getItem();
			return Collections.singletonList(new ItemStack(item, x + (bl ? 1 : 0)));
		}

		return Collections.emptyList();
	}

}
