package steve_gall.minecolonies_compatibility.module.common.regions_unexplored;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.regions_unexplored.block.RuBlocks;
import net.regions_unexplored.world.level.block.leaves.AppleLeavesBlock;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class AppleLeavesFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return RuBlocks.APPLE_OAK_LEAVES.getId();
	}

	@Override
	public @NotNull List<ItemLike> getBlockIcons()
	{
		return Arrays.asList(RuBlocks.APPLE_OAK_SAPLING.get(), RuBlocks.APPLE_OAK_LEAVES.get());
	}

	@Override
	public @NotNull List<Item> getItemIcons()
	{
		return Arrays.asList(Items.APPLE);
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == RuBlocks.APPLE_OAK_LEAVES.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(AppleLeavesBlock.AGE) == AppleLeavesBlock.MAX_AGE;
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
			level.setBlock(context.getPosition(), context.getState().setValue(AppleLeavesBlock.AGE, 0), Block.UPDATE_CLIENTS);
		}

		return Collections.singletonList(new ItemStack(Items.APPLE, 1));
	}

}
