package steve_gall.minecolonies_compatibility.core.common.module.blue_skies;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.legacy.blue_skies.blocks.natural.BrewberryBushBlock;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.core.common.mixin.blue_skies.BrewberryBushBlockAccessor;

public class BrewBerryFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof BrewberryBushBlock;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(BrewberryBushBlock.MATURE);
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context)
	{
		if (context.getLevel() instanceof Level level)
		{
			var state = context.getState();
			level.setBlock(context.getPosition(), state.setValue(BrewberryBushBlock.MATURE, false), Block.UPDATE_CLIENTS);
			var result = ((BrewberryBushBlockAccessor) state.getBlock()).invokeGetBerry(level, state, context.getPosition());

			return Collections.singletonList(result);
		}

		return Collections.emptyList();
	}

}
