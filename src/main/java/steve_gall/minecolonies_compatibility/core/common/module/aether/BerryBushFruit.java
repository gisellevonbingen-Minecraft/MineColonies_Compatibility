package steve_gall.minecolonies_compatibility.core.common.module.aether;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.aetherteam.aether.block.AetherBlockStateProperties;
import com.aetherteam.aether.block.AetherBlocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.PlantBlockContext;

public class BerryBushFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		var block = context.getState().getBlock();
		return block == AetherBlocks.BERRY_BUSH_STEM.get() || block == AetherBlocks.BERRY_BUSH.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		var block = context.getState().getBlock();
		return block == AetherBlocks.BERRY_BUSH.get();
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof LevelWriter level)
		{
			var state = context.getState();
			var property = AetherBlockStateProperties.DOUBLE_DROPS;
			var newState = AetherBlocks.BERRY_BUSH_STEM.get().defaultBlockState().setValue(property, state.getValue(property));
			level.setBlock(context.getPosition(), newState, Block.UPDATE_ALL);
		}

		return context.getDrops(null);
	}

}
