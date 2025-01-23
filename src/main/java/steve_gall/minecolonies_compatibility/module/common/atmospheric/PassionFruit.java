package steve_gall.minecolonies_compatibility.module.common.atmospheric;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.teamabnormals.atmospheric.common.block.PassionVineBlock;
import com.teamabnormals.atmospheric.core.registry.AtmosphericBlocks;
import com.teamabnormals.atmospheric.core.registry.AtmosphericItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class PassionFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return AtmosphericItems.PASSION_FRUIT.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Collections.singletonList(new ItemStack(AtmosphericBlocks.PASSION_VINE.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Collections.singletonList(new ItemStack(AtmosphericItems.PASSION_FRUIT.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == AtmosphericBlocks.PASSION_VINE.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(PassionVineBlock.AGE) == 4;
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
			var state = context.getState();
			var count = 1 + level.random.nextInt(2) + level.random.nextInt(2) + level.random.nextInt(3);
			level.setBlock(context.getPosition(), state.setValue(PassionVineBlock.AGE, 1), Block.UPDATE_CLIENTS);

			return Collections.singletonList(new ItemStack(AtmosphericItems.PASSION_FRUIT.get(), count));
		}
		else
		{
			return Collections.emptyList();
		}

	}

}
