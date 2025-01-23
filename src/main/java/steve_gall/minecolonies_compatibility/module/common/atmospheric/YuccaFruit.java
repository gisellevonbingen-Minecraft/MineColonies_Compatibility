package steve_gall.minecolonies_compatibility.module.common.atmospheric;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;
import com.teamabnormals.atmospheric.core.registry.AtmosphericBlocks;
import com.teamabnormals.atmospheric.core.registry.AtmosphericItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class YuccaFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return AtmosphericItems.YUCCA_FRUIT.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Arrays.asList(new ItemStack(AtmosphericBlocks.YUCCA_BRANCH.get()), new ItemStack(AtmosphericBlocks.YUCCA_BUNDLE.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Collections.singletonList(new ItemStack(AtmosphericItems.YUCCA_FRUIT.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		var state = context.getState();

		if (state.is(AtmosphericBlocks.YUCCA_BUNDLE.get()))
		{
			var above = context.getLevel().getBlockState(context.getPosition().above());
			return above.is(AtmosphericBlocks.YUCCA_BRANCH.get());
		}
		else if (state.is(AtmosphericBlocks.YUCCA_BRANCH.get()))
		{
			var below = context.getLevel().getBlockState(context.getPosition().below());
			return below.isAir();
		}

		return false;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().is(AtmosphericBlocks.YUCCA_BUNDLE.get());
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public IToolType getHarvestToolType()
	{
		return ToolType.AXE;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof ServerLevel level)
		{
			level.setBlockAndUpdate(context.getPosition(), Blocks.AIR.defaultBlockState());
		}

		return context.getDrops(harvester);
	}

}
