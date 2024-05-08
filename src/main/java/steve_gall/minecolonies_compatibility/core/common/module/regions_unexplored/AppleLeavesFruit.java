package steve_gall.minecolonies_compatibility.core.common.module.regions_unexplored;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.regions_unexplored.world.level.block.leaves.AppleLeavesBlock;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.PlantBlockContext;

public class AppleLeavesFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof AppleLeavesBlock;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(AppleLeavesBlock.AGE) == AppleLeavesBlock.MAX_AGE;
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
