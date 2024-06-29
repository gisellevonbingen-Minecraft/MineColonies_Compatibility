package steve_gall.minecolonies_compatibility.api.common.building.module;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.buildings.modules.ICraftingBuildingModule;
import com.minecolonies.api.crafting.IRecipeStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface ICraftingModuleWithExternalWorkingBlocks extends ICraftingBuildingModule, IModuleWithExternalWorkingBlocks
{
	boolean isIntermediate(@NotNull Block intermediateBlock);

	default boolean hasWorkingBlock(@NotNull IRecipeStorage recipeStorage)
	{
		return this.isIntermediate(recipeStorage.getIntermediate());
	}

	@NotNull
	Component getWorkingBlockNotFoundMessage();

	@NotNull
	default Component getWorkingBlockNotFoundMessage(@NotNull IRecipeStorage recipeStorage)
	{
		return this.getWorkingBlockNotFoundMessage();
	}

	@NotNull
	default BlockPos getHitPosition(@NotNull BlockPos pos)
	{
		return pos;
	}

	@NotNull
	default BlockPos getParticlePosition(@NotNull BlockPos pos)
	{
		return this.getHitPosition(pos).above();
	}

	@NotNull
	default Stream<BlockPos> getRecipeWorkingBlocks(@NotNull IRecipeStorage recipeStorage)
	{
		var level = this.getBuilding().getColony().getWorld();

		return this.getWorkingBlocks().filter(pos ->
		{
			var state = level.getBlockState(pos);
			return this.canBlockRecipeWorking(level, pos, state, recipeStorage);
		});
	}

	default boolean canBlockRecipeWorking(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IRecipeStorage recipeStorage)
	{
		return true;
	}

}
