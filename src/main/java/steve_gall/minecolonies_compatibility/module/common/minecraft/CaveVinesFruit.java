package steve_gall.minecolonies_compatibility.module.common.minecraft;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class CaveVinesFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return ForgeRegistries.BLOCKS.getKey(Blocks.CAVE_VINES);
	}

	@Override
	public @NotNull List<ItemLike> getBlockIcons()
	{
		return Arrays.asList(Items.GLOW_BERRIES);
	}

	@Override
	public @NotNull List<Item> getItemIcons()
	{
		return Arrays.asList(Items.GLOW_BERRIES);
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		var block = context.getState().getBlock();
		return block == Blocks.CAVE_VINES || block == Blocks.CAVE_VINES_PLANT;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		var state = context.getState();

		if (state.getValue(CaveVines.BERRIES))
		{
			return true;
		}
		else if (state.getBlock() == Blocks.CAVE_VINES)
		{
			if (((GrowingPlantHeadBlock) state.getBlock()).isMaxAge(state))
			{
				return true;
			}
			else
			{
				var belowPos = context.getPosition().below();
				var belowState = context.getLevel().getBlockState(belowPos);
				return !belowState.isAir();
			}

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
		return state.getValue(CaveVines.BERRIES) ? SoundEvents.CAVE_VINES_PICK_BERRIES : state.getSoundType(context.getLevel(), context.getPosition(), null).getBreakSound();
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		var state = context.getState();
		var position = context.getPosition();

		if (state.getValue(CaveVines.BERRIES))
		{
			if (context.getLevel() instanceof LevelWriter level)
			{
				level.setBlock(position, state.setValue(CaveVines.BERRIES, false), Block.UPDATE_CLIENTS);
			}

			return Collections.singletonList(new ItemStack(Items.GLOW_BERRIES));
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
