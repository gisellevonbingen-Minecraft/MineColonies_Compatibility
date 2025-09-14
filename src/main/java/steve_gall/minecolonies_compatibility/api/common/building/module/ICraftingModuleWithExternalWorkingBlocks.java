package steve_gall.minecolonies_compatibility.api.common.building.module;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.buildings.modules.ICraftingBuildingModule;
import com.minecolonies.api.crafting.IRecipeStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.core.common.util.InteractionMessageHelper;

public interface ICraftingModuleWithExternalWorkingBlocks extends ICraftingBuildingModule, IModuleWithExternalWorkingBlocks
{
	default boolean needWorkingBlock(@NotNull IRecipeStorage recipeStorage)
	{
		return true;
	}

	@NotNull
	default Component getWorkingBlockNotFoundMessage(@NotNull IRecipeStorage recipeStorage)
	{
		if (recipeStorage.getIntermediate() == Blocks.AIR)
		{
			return InteractionMessageHelper.getWorkingBlockNotFound();
		}

		return InteractionMessageHelper.getWorkingBlockNotFound(recipeStorage.getIntermediate());
	}

	@NotNull
	default BlockPos getHitPosition(@NotNull BlockPos pos)
	{
		return pos;
	}

	@NotNull
	default BlockPos getParticlePosition(@NotNull BlockPos pos)
	{
		return this.getHitPosition(pos);
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

	default boolean canBlockRecipeWorking(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IRecipeStorage recipeStorage)
	{
		return true;
	}

}
