package steve_gall.minecolonies_compatibility.module.common.undergarden;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import quek.undergarden.block.Droopvine;
import quek.undergarden.registry.UGBlocks;
import quek.undergarden.registry.UGItems;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class DroopvineFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return UGBlocks.DROOPVINE.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Arrays.asList(new ItemStack(UGItems.DROOPFRUIT.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Arrays.asList(new ItemStack(UGItems.DROOPFRUIT.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		var block = context.getState().getBlock();
		return block == UGBlocks.DROOPVINE.get() || block == UGBlocks.DROOPVINE_PLANT.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		var state = context.getState();

		if (state.getValue(Droopvine.GLOWY))
		{
			return true;
		}
		else if (state.getBlock() == UGBlocks.DROOPVINE.get())
		{
			var belowPos = context.getPosition().below();
			var belowState = context.getLevel().getBlockState(belowPos);
			return !belowState.isAir();
		}

		return false;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public @NotNull SoundEvent getHarvestSound(@NotNull PlantBlockContext context)
	{
		var state = context.getState();
		return state.getValue(Droopvine.GLOWY) ? SoundEvents.CAVE_VINES_PICK_BERRIES : state.getSoundType(context.getLevel(), context.getPosition(), null).getBreakSound();
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		var state = context.getState();
		var position = context.getPosition();

		if (state.getValue(Droopvine.GLOWY))
		{
			if (context.getLevel() instanceof LevelWriter level)
			{
				level.setBlock(position, state.setValue(Droopvine.GLOWY, false), Block.UPDATE_CLIENTS);
			}

			return Collections.singletonList(new ItemStack(UGItems.DROOPFRUIT.get()));
		}
		else
		{
			if (context.getLevel() instanceof LevelWriter level)
			{
				level.setBlock(position, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
			}

			return Collections.emptyList();
		}

	}

}
