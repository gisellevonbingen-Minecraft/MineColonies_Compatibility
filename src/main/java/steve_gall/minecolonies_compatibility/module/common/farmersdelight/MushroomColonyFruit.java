package steve_gall.minecolonies_compatibility.module.common.farmersdelight;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;

public class MushroomColonyFruit extends CustomizedFruit
{
	private final MushroomColonyBlock block;

	public MushroomColonyFruit(MushroomColonyBlock block)
	{
		this.block = block;
	}

	@Override
	public @NotNull ResourceLocation getId()
	{
		return BuiltInRegistries.BLOCK.getKey(this.block);
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Arrays.asList(new ItemStack(this.block));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Arrays.asList(this.block.getCloneItemStack(null, null, null));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == this.getBlock();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(MushroomColonyBlock.COLONY_AGE) > 0;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(MushroomColonyBlock.COLONY_AGE) == this.block.getMaxAge();
	}

	@Override
	public @NotNull SoundEvent getHarvestSound(@NotNull PlantBlockContext context)
	{
		return SoundEvents.MOOSHROOM_SHEAR;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof LevelWriter level)
		{
			var state = context.getState();
			var age = state.getValue(MushroomColonyBlock.COLONY_AGE);
			level.setBlock(context.getPosition(), state.setValue(MushroomColonyBlock.COLONY_AGE, age - 1), Block.UPDATE_CLIENTS);
		}

		return Collections.singletonList(this.block.getCloneItemStack(context.getLevel(), context.getPosition(), context.getState()));
	}

	public MushroomColonyBlock getBlock()
	{
		return this.block;
	}

}
