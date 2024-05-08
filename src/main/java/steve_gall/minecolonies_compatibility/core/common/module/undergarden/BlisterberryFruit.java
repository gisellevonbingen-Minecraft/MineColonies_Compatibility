package steve_gall.minecolonies_compatibility.core.common.module.undergarden;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import quek.undergarden.block.BlisterberryBushBlock;
import quek.undergarden.registry.UGItems;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class BlisterberryFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof BlisterberryBushBlock;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(BlisterberryBushBlock.AGE) > 1;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof ServerLevel level)
		{
			var state = context.getState();
			var age = state.getValue(BlisterberryBushBlock.AGE);
			var flag = age == 3;
			var random = 1 + level.getRandom().nextInt(2);
			var random2 = level.getRandom().nextInt(2);

			var newState = state.setValue(BlisterberryBushBlock.AGE, 1);
			level.setBlock(context.getPosition(), newState, Block.UPDATE_CLIENTS);

			var drops = new ArrayList<ItemStack>();
			drops.add(new ItemStack(UGItems.BLISTERBERRY.get(), random + (flag ? 1 : 0)));
			drops.add(new ItemStack(UGItems.ROTTEN_BLISTERBERRY.get(), random2 + (flag ? 1 : 0)));
			return drops;
		}

		return Collections.emptyList();
	}

}
