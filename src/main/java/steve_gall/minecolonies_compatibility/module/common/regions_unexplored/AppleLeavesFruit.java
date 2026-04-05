package steve_gall.minecolonies_compatibility.module.common.regions_unexplored;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.regions_unexplored.registry.RUBlocks;
import net.regions_unexplored.world.level.block.leaves.AppleLeavesBlock;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class AppleLeavesFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return BuiltInRegistries.BLOCK.getKey(RUBlocks.APPLE_OAK_NATURAL_SET.getLeaves());
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Arrays.asList(new ItemStack(RUBlocks.APPLE_OAK_NATURAL_SET.getSapling()), new ItemStack(RUBlocks.APPLE_OAK_NATURAL_SET.getLeaves()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Arrays.asList(new ItemStack(Items.APPLE));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == RUBlocks.APPLE_OAK_NATURAL_SET.getLeaves();
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
