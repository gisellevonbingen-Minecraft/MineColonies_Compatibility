package steve_gall.minecolonies_compatibility.module.common.nethersdelight;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import umpaz.nethersdelight.common.block.PropelplantBerryCaneBlock;
import umpaz.nethersdelight.common.registry.NDBlocks;
import umpaz.nethersdelight.common.registry.NDItems;

public class PropelpPearlFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return NDItems.PROPELPEARL.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Arrays.asList(new ItemStack(NDBlocks.PROPELPLANT_CANE.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Arrays.asList(new ItemStack(NDItems.PROPELPEARL.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().is(NDBlocks.PROPELPLANT_BERRY_STEM.get());
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(PropelplantBerryCaneBlock.PEARL);
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof ServerLevel level)
		{
			var newState = context.getState().setValue(PropelplantBerryCaneBlock.PEARL, false);
			level.setBlock(context.getPosition(), newState, Block.UPDATE_CLIENTS);

			var count = 1 + level.random.nextInt(2);
			return Collections.singletonList(new ItemStack(NDItems.PROPELPEARL.get(), count));
		}
		else
		{
			return Collections.emptyList();
		}

	}

}
