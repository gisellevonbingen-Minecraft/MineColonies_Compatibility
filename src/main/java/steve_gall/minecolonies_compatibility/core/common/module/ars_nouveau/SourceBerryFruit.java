package steve_gall.minecolonies_compatibility.core.common.module.ars_nouveau;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.hollingsworth.arsnouveau.common.block.SourceBerryBush;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class SourceBerryFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == BlockRegistry.SOURCEBERRY_BUSH.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(SourceBerryBush.AGE) > 1;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(SourceBerryBush.AGE) == 3;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof ServerLevel level)
		{
			var state = context.getState();
			var age = state.getValue(SourceBerryBush.AGE);
			var count = 1 + level.random.nextInt(2) + (age == 3 ? 1 : 0);
			level.setBlock(context.getPosition(), state.setValue(SourceBerryBush.AGE, 1), Block.UPDATE_CLIENTS);

			return Collections.singletonList(new ItemStack(BlockRegistry.SOURCEBERRY_BUSH, count));
		}
		else
		{
			return Collections.emptyList();
		}

	}

}
