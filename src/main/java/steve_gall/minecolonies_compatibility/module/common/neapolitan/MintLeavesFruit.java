package steve_gall.minecolonies_compatibility.module.common.neapolitan;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.teamabnormals.neapolitan.common.block.MintBlock;
import com.teamabnormals.neapolitan.core.registry.NeapolitanBlocks;
import com.teamabnormals.neapolitan.core.registry.NeapolitanItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class MintLeavesFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return NeapolitanItems.MINT_LEAVES.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Collections.singletonList(new ItemStack(NeapolitanBlocks.MINT.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Collections.singletonList(new ItemStack(NeapolitanItems.MINT_LEAVES.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == NeapolitanBlocks.MINT.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		var state = context.getState();
		return state.getBlock() instanceof MintBlock block && block.isMaxAge(state);
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		var state = context.getState();
		var sprouts = state.getValue(MintBlock.SPROUTS);
		var drops = sprouts;

		if (context.getLevel() instanceof LevelWriter level)
		{
			var newState = state.setValue(MintBlock.AGE, 1);
			var maxSprouts = 4;

			if (sprouts < maxSprouts)
			{
				var sproutsWithDrops = sprouts + drops;
				newState = newState.setValue(MintBlock.SPROUTS, Math.min(maxSprouts, sproutsWithDrops));
				drops = sproutsWithDrops - newState.getValue(MintBlock.SPROUTS);
			}

			level.setBlock(context.getPosition(), newState, Block.UPDATE_CLIENTS);
		}

		if (drops > 0)
		{
			return Collections.singletonList(new ItemStack(NeapolitanItems.MINT_LEAVES.get(), drops));
		}
		else
		{
			return Collections.emptyList();
		}

	}

	@Override
	public @NotNull SoundEvent getHarvestSound(@NotNull PlantBlockContext context)
	{
		return SoundEvents.CROP_BREAK;
	}

}
