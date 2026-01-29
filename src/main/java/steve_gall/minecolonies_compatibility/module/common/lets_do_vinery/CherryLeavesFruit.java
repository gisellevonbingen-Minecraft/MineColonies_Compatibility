package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.satisfy.vinery.core.block.DarkCherryLeavesBlock;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class CherryLeavesFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return ObjectRegistry.DARK_CHERRY_LEAVES.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Arrays.asList(new ItemStack(ObjectRegistry.DARK_CHERRY_SAPLING.get()), new ItemStack(ObjectRegistry.DARK_CHERRY_LEAVES.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Arrays.asList(new ItemStack(ObjectRegistry.CHERRY.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == ObjectRegistry.DARK_CHERRY_LEAVES.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		var state = context.getState();
		return state.getValue(DarkCherryLeavesBlock.CAN_GROW_CHERRIES) && state.getValue(DarkCherryLeavesBlock.HAS_CHERRIES);
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
			var random = level.getRandom();
			var dropCount = random.nextBoolean() ? random.nextInt(1, 4) : 1;
			var dropStack = new ItemStack(ObjectRegistry.CHERRY.get(), dropCount);

			var newState = context.getState().setValue(DarkCherryLeavesBlock.HAS_CHERRIES, false);
			level.setBlockAndUpdate(context.getPosition(), newState);

			return Collections.singletonList(dropStack);
		}

		return Collections.emptyList();
	}

}
