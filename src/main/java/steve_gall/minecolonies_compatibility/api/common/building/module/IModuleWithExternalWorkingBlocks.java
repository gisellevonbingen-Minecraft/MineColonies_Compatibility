package steve_gall.minecolonies_compatibility.api.common.building.module;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.buildings.modules.IModuleWithExternalBlocks;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.PathJobFindWorkingBlocks;

public interface IModuleWithExternalWorkingBlocks extends IModuleWithExternalBlocks
{
	@NotNull
	PathJobFindWorkingBlocks<?> createWorkingBlocksFindPath(@Nullable AbstractEntityCitizen citizen);

	/**
	 *
	 * @param citizen
	 * @return true if finding path
	 */
	boolean requestFindWorkingBlocks(@Nullable AbstractEntityCitizen citizen);

	boolean addWorkingBlock(@Nullable BlockPos pos);

	boolean removeWorkingBlock(@Nullable BlockPos pos);

	@NotNull
	Component getWorkingBlockNotFoundMessage();

	@NotNull
	default Stream<BlockPos> getWorkingBlocks(@NotNull LevelReader level)
	{
		return this.getRegisteredBlocks().stream().filter(pos ->
		{
			var state = level.getBlockState(pos);

			if (this.isWorkingBlock(level, pos, state))
			{
				return true;
			}

			this.removeWorkingBlock(pos);
			return false;
		});
	}

	default boolean isWorkingBlock(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state)
	{
		return this.isIntermediate(state.getBlock());
	}

	boolean isIntermediate(@NotNull Block block);

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
