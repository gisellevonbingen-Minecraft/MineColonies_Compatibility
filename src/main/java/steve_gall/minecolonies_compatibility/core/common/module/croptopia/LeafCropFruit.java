package steve_gall.minecolonies_compatibility.core.common.module.croptopia;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.epherical.croptopia.blocks.LeafCropBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;

public class LeafCropFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull BlockState state)
	{
		return state.getBlock() instanceof LeafCropBlock;
	}

	@Override
	public boolean canHarvest(@NotNull BlockState state)
	{
		return state.getBlock() instanceof LeafCropBlock block && state.getValue(LeafCropBlock.AGE) == block.getMaxAge();
	}

	@Override
	@NotNull
	public List<ItemStack> harvest(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos position)
	{
		if (state.getBlock() instanceof LeafCropBlock block)
		{
			level.setBlock(position, block.getStateForAge(0), Block.UPDATE_CLIENTS);

			if (level instanceof ServerLevel serverLevel)
			{
				return Block.getDrops(state, serverLevel, position, null);
			}

		}

		return Collections.emptyList();
	}

}
