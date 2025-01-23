package steve_gall.minecolonies_compatibility.module.common.atmospheric;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.teamabnormals.atmospheric.common.block.BarrelCactusBlock;
import com.teamabnormals.atmospheric.core.registry.AtmosphericBlocks;
import com.teamabnormals.atmospheric.core.registry.AtmosphericItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class BarrelCactusFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return AtmosphericItems.BARREL_CACTUS.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Collections.singletonList(new ItemStack(AtmosphericBlocks.BARREL_CACTUS.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Collections.singletonList(new ItemStack(AtmosphericItems.BARREL_CACTUS.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == AtmosphericBlocks.BARREL_CACTUS.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(BarrelCactusBlock.AGE) > 0;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(BarrelCactusBlock.AGE) == 3;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof LevelWriter level)
		{
			var state = context.getState();
			var count = state.getValue(BarrelCactusBlock.AGE);
			level.setBlock(context.getPosition(), state.setValue(BarrelCactusBlock.AGE, 0), Block.UPDATE_CLIENTS);

			return Collections.singletonList(new ItemStack(AtmosphericItems.BARREL_CACTUS.get(), count));
		}
		else
		{
			return Collections.emptyList();
		}

	}

	@Override
	public @NotNull SoundEvent getHarvestSound(@NotNull PlantBlockContext context)
	{
		return context.getState().getSoundType().getBreakSound();
	}

}
