package steve_gall.minecolonies_compatibility.core.common.module.pamhc2trees;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.pam.pamhc2trees.blocks.BlockPamFruit;
import com.pam.pamhc2trees.blocks.BlockPamLogFruit;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;

public class PamFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull BlockState state)
	{
		var block = state.getBlock();
		return block instanceof BlockPamFruit || block instanceof BlockPamLogFruit;
	}

	@Override
	public boolean canHarvest(@NotNull BlockState state)
	{
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
	@NotNull
	public List<ItemStack> harvest(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos position)
	{
		level.setBlock(position, state.getBlock().defaultBlockState(), Block.UPDATE_CLIENTS);

		if (level instanceof ServerLevel serverLevel)
		{
			return Block.getDrops(state, serverLevel, position, null);
		}

		return Collections.emptyList();
	}

}
