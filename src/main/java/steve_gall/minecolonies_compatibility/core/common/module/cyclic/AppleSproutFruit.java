package steve_gall.minecolonies_compatibility.core.common.module.cyclic;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.lothrazar.cyclic.block.apple.AppleCropBlock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.core.common.mixin.cyclic.AppleCropBlockAccessor;

public class AppleSproutFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof AppleCropBlock;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(AppleCropBlockAccessor.getAge()) >= AppleCropBlockAccessor.getMaxAge();
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		var block = context.getState().getBlock();
		// For apply fortune
		// https://github.com/Lothrazar/Cyclic/blob/trunk/1.20.1/src/main/resources/data/cyclic/loot_tables/blocks/apple_sprout.json
		var drops = new ArrayList<>(context.getDrops(harvester));
		var canReplant = false;

		for (var stack : drops)
		{
			if (stack.getItem() instanceof BlockItem item && item.getBlock() == block)
			{
				canReplant = true;
				stack.shrink(1);
				break;
			}

		}

		if (canReplant)
		{
			drops.removeIf(ItemStack::isEmpty);
		}

		if (context.getLevel() instanceof LevelWriter level)
		{
			level.setBlock(context.getPosition(), (canReplant ? block : Blocks.AIR).defaultBlockState(), Block.UPDATE_CLIENTS);
		}

		return drops;
	}

}
