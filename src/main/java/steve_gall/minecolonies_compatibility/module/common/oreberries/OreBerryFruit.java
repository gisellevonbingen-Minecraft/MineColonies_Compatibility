package steve_gall.minecolonies_compatibility.module.common.oreberries;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.mrbysco.oreberriesreplanted.block.OreBerryBushBlock;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.mixin.common.oreberries.OreBerryBushBlockAccessor;

public class OreBerryFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof OreBerryBushBlock;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		var state = context.getState();
		return ((OreBerryBushBlock) state.getBlock()).isMaxAge(state);
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
			var state = context.getState();
			var block = (OreBerryBushBlock) state.getBlock();
			level.setBlock(context.getPosition(), block.withAge(block.getMaxAge() - 1), Block.UPDATE_ALL);

			var drop = new ItemStack(((OreBerryBushBlockAccessor) block).invokeGetBerryItem(), level.random.nextInt(3) + 1);
			return Collections.singletonList(drop);
		}

		return Collections.emptyList();
	}

}
