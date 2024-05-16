package steve_gall.minecolonies_compatibility.api.common.building.module;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.buildings.modules.IModuleWithExternalBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface IModuleWithExternalWorkingBlocks extends IModuleWithExternalBlocks
{
	boolean unRegister(@Nullable BlockPos pos);

	@NotNull
	Component getWorkingBlockNotFoundMessage();

	@Nullable
	default BlockPos findWorkingBlock(@NotNull LevelReader level)
	{
		return this.findWorkingBlocks(level).findAny().orElse(null);
	}

	@NotNull
	default Stream<BlockPos> findWorkingBlocks(@NotNull LevelReader level)
	{
		return this.getRegisteredBlocks().stream().filter(pos ->
		{
			var state = level.getBlockState(pos);

			if (this.testWorking(level, pos, state))
			{
				return true;
			}

			this.unRegister(pos);
			return false;
		});
	}

	default boolean testWorking(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state)
	{
		return this.testIntermediate(state.getBlock());
	}

	boolean testIntermediate(@NotNull Block block);

	@NotNull
	default BlockPos getWalkingPosition(@NotNull LevelReader level, @NotNull BlockPos pos)
	{
		return pos;
	}

	@NotNull
	default BlockPos getHitPosition(@NotNull LevelReader level, @NotNull BlockPos pos)
	{
		return pos;
	}

	@NotNull
	default BlockPos getParticlePosition(@NotNull LevelReader level, @NotNull BlockPos pos)
	{
		return this.getHitPosition(level, pos).above();
	}

}
