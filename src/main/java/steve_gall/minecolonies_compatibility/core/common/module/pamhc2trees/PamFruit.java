package steve_gall.minecolonies_compatibility.core.common.module.pamhc2trees;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.pam.pamhc2trees.blocks.BlockPamFruit;
import com.pam.pamhc2trees.blocks.BlockPamLogFruit;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class PamFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		var block = context.getState().getBlock();
		return block instanceof BlockPamFruit || block instanceof BlockPamLogFruit;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		var state = context.getState();
		var block = state.getBlock();

		if (block instanceof BlockPamFruit fruit && fruit.isMaxAge(state))
		{
			return true;
		}
		else if (block instanceof BlockPamLogFruit logFruit && logFruit.isMaxAge(state))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	@NotNull
	public List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof LevelWriter level)
		{
			var newState = context.getState().getBlock().defaultBlockState();
			level.setBlock(context.getPosition(), newState, Block.UPDATE_CLIENTS);
		}

		return context.getDrops(null);
	}

}
