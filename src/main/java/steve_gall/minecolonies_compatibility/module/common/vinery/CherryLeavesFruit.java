package steve_gall.minecolonies_compatibility.module.common.vinery;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import satisfyu.vinery.block.CherryLeaves;
import satisfyu.vinery.registry.ObjectRegistry;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class CherryLeavesFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return ObjectRegistry.CHERRY_LEAVES.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Arrays.asList(new ItemStack(ObjectRegistry.CHERRY_SAPLING.get()), new ItemStack(ObjectRegistry.CHERRY_LEAVES.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Arrays.asList(new ItemStack(ObjectRegistry.CHERRY.get()), new ItemStack(ObjectRegistry.ROTTEN_CHERRY.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == ObjectRegistry.CHERRY_LEAVES.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		var state = context.getState();
		return state.getValue(CherryLeaves.VARIANT) && state.getValue(CherryLeaves.HAS_CHERRIES);
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
			var dropCount = random.nextBoolean() ? Mth.nextInt(random, 1, 3) : 1;
			var rotten = random.nextInt(8) == 0;
			var dropStack = new ItemStack(rotten ? ObjectRegistry.ROTTEN_CHERRY.get() : ObjectRegistry.CHERRY.get(), dropCount);

			var newState = context.getState().setValue(CherryLeaves.HAS_CHERRIES, false);
			level.setBlockAndUpdate(context.getPosition(), newState);

			return Collections.singletonList(dropStack);
		}

		return Collections.emptyList();
	}

}
