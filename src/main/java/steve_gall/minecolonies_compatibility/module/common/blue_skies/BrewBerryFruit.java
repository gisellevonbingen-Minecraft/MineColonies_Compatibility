package steve_gall.minecolonies_compatibility.module.common.blue_skies;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.legacy.blue_skies.blocks.natural.BrewberryBushBlock;
import com.legacy.blue_skies.registries.SkiesBlocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.mixin.common.blue_skies.BrewberryBushBlockAccessor;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;

public class BrewBerryFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return new ResourceLocation(ModuleManager.BLUE_SKIES.getModId(), "brewberry_bush");
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Arrays.asList(new ItemStack(SkiesBlocks.brewberry_bush));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Arrays.stream(BrewberryBushBlock.Type.values()).map(BrewberryBushBlock.Type::getBerry).map(ItemStack::new).toList();
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == SkiesBlocks.brewberry_bush;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(BrewberryBushBlock.MATURE);
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
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
