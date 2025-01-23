package steve_gall.minecolonies_compatibility.module.common.neapolitan;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.teamabnormals.neapolitan.common.block.AdzukiSproutsBlock;
import com.teamabnormals.neapolitan.core.registry.NeapolitanBlocks;
import com.teamabnormals.neapolitan.core.registry.NeapolitanItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class AdzukiSproutsFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return NeapolitanItems.ADZUKI_BEANS.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Collections.singletonList(new ItemStack(NeapolitanItems.ADZUKI_BEANS.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Collections.singletonList(new ItemStack(NeapolitanItems.ADZUKI_BEANS.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == NeapolitanBlocks.ADZUKI_SPROUTS.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		var state = context.getState();
		return state.getBlock() instanceof AdzukiSproutsBlock block && block.isMaxAge(state);
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof LevelWriter level)
		{
			level.setBlock(context.getPosition(), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
			level.setBlock(context.getPosition().below(), NeapolitanBlocks.ADZUKI_SOIL.get().defaultBlockState(), Block.UPDATE_ALL);
		}

		return context.getDrops(harvester);
	}

	@Override
	public @NotNull SoundEvent getHarvestSound(@NotNull PlantBlockContext context)
	{
		return SoundEvents.CROP_BREAK;
	}

}
