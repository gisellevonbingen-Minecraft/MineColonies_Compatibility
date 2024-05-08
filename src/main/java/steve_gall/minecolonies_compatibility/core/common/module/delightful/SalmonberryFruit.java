package steve_gall.minecolonies_compatibility.core.common.module.delightful;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.brnbrd.delightful.common.block.DelightfulBlocks;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.PlantBlockContext;

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
		return SalmonberryCrop.canHarvest(context);
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		return SalmonberryCrop.harvest(context, harvester);
	}

}
