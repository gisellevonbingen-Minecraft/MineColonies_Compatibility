package steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.building.module.AbstractCraftingModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.PathJobFindWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.WorkingBlocksPathResult;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleCraftingTypes;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class CookingCraftingModule extends AbstractCraftingModuleWithExternalWorkingBlocks
{
	public CookingCraftingModule(JobEntry jobEntry)
	{
		super(jobEntry);
	}

	@Override
	public boolean isWorkingBlock(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state)
	{
		return level.getBlockEntity(pos) instanceof CookingPotBlockEntity cookingPot && cookingPot.isHeated();
	}

	@Override
	public boolean isIntermediate(@NotNull Block intermediateBlock)
	{
		return intermediateBlock == ModBlocks.COOKING_POT.get();
	}

	@Override
	public @NotNull BlockPos getWalkingPosition(@NotNull BlockPos pos)
	{
		return pos.below();
	}

	@Override
	public @NotNull BlockPos getParticlePosition(@NotNull BlockPos pos)
	{
		return pos.below().below();
	}

	@Override
	public @NotNull String getId()
	{
		return "farmers_cooking";
	}

	@Override
	public Set<CraftingType> getSupportedCraftingTypes()
	{
		return Collections.singleton(ModuleCraftingTypes.COOKING.get());
	}

	@Override
	public boolean isRecipeCompatible(@NotNull IGenericRecipe recipe)
	{
		return true;
	}

	@Override
	@NotNull
	public Component getWorkingBlockNotFoundMessage()
	{
		return Component.translatable("minecolonies_compatibility.interaction.no_heated_cooking_pot");
	}

	@Override
	public WorkingBlocksPathResult createPathResult(@Nullable AbstractEntityCitizen citizen)
	{
		return new WorkingBlocksPathResult(this)
		{
			@Override
			public boolean test(@NotNull PathJobFindWorkingBlocks<?> job, @NotNull BlockPos.MutableBlockPos pos)
			{
				return super.test(job, pos) || super.test(job, pos.setY(pos.getY() + 1));
			}

		};
	}

}
