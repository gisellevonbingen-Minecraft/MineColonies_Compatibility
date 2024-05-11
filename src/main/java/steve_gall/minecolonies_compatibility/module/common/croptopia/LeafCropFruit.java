package steve_gall.minecolonies_compatibility.module.common.croptopia;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.epherical.croptopia.blocks.LeafCropBlock;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class LeafCropFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof LeafCropBlock;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		var state = context.getState();
		return state.getBlock() instanceof LeafCropBlock block && block.isMaxAge(state);
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
			var newState = ((LeafCropBlock) context.getState().getBlock()).getStateForAge(0);
			level.setBlock(context.getPosition(), newState, Block.UPDATE_CLIENTS);
		}

		return context.getDrops(null);
	}

}
