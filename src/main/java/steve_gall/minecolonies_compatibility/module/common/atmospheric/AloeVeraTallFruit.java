package steve_gall.minecolonies_compatibility.module.common.atmospheric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.teamabnormals.atmospheric.common.block.AloeVeraBlock;
import com.teamabnormals.atmospheric.common.block.AloeVeraTallBlock;
import com.teamabnormals.atmospheric.core.registry.AtmosphericBlocks;
import com.teamabnormals.atmospheric.core.registry.AtmosphericItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class AloeVeraTallFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return AtmosphericBlocks.TALL_ALOE_VERA.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Collections.singletonList(new ItemStack(AtmosphericBlocks.ALOE_VERA.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Arrays.asList(new ItemStack(AtmosphericItems.ALOE_LEAVES.get()), new ItemStack(AtmosphericItems.YELLOW_BLOSSOMS.get()), new ItemStack(AtmosphericItems.ALOE_KERNELS.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		var state = context.getState();
		return state.getBlock() == AtmosphericBlocks.TALL_ALOE_VERA.get() && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(AloeVeraTallBlock.AGE) == 8;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof ServerLevel level)
		{
			var state = context.getState();
			var drops = new ArrayList<ItemStack>();
			drops.add(new ItemStack(AtmosphericItems.YELLOW_BLOSSOMS.get(), state.getValue(AloeVeraTallBlock.AGE) - 5));
			drops.add(new ItemStack(AtmosphericItems.ALOE_KERNELS.get()));
			drops.add(new ItemStack(AtmosphericItems.ALOE_LEAVES.get(), 3 + level.random.nextInt(5)));

			level.setBlock(context.getPosition(), AtmosphericBlocks.ALOE_VERA.get().defaultBlockState().setValue(AloeVeraBlock.AGE, 2), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
			level.setBlock(context.getPosition().above(), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
			return drops;
		}
		else
		{
			return Collections.emptyList();
		}

	}

	@Override
	public @NotNull SoundEvent getHarvestSound(@NotNull PlantBlockContext context)
	{
		return SoundEvents.SHEEP_SHEAR;
	}

}
