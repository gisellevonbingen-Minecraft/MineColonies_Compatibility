package steve_gall.minecolonies_compatibility.module.common.cyclic;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.lothrazar.cyclic.block.apple.AppleCropBlock;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.mixin.common.cyclic.AppleCropBlockAccessor;

public class AppleSproutFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof AppleCropBlock;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(AppleCropBlockAccessor.getAge()) >= AppleCropBlockAccessor.getMaxAge();
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		// For apply fortune
		// https://github.com/Lothrazar/Cyclic/blob/trunk/1.20.1/src/main/resources/data/cyclic/loot_tables/blocks/apple_sprout.json
		var drops = context.getDrops(harvester);
		var newState = context.getState().getBlock().defaultBlockState();
		this.replant(context, drops, newState);
		return drops;
	}

}
