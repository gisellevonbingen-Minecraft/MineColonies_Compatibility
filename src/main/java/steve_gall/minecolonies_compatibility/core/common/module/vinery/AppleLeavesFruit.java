package steve_gall.minecolonies_compatibility.core.common.module.vinery;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import satisfyu.vinery.block.AppleLeaves;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.PlantBlockContext;

public class AppleLeavesFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof AppleLeaves;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		var state = context.getState();
		return state.getValue(AppleLeaves.VARIANT) && state.getValue(AppleLeaves.HAS_APPLES);
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof ServerLevel level)
		{
			var random = level.getRandom();
			var dropCount = random.nextBoolean() ? Mth.nextInt(random, 1, 3) : 1;
			var dropStack = new ItemStack(Items.APPLE, dropCount);

			var newState = context.getState().setValue(AppleLeaves.HAS_APPLES, false);
			level.setBlockAndUpdate(context.getPosition(), newState);

			return Collections.singletonList(dropStack);
		}

		return Collections.emptyList();
	}

}
