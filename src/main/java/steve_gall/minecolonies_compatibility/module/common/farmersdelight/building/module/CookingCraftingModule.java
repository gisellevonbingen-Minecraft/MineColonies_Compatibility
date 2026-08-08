package steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableSet;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.building.module.AbstractCraftingModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.building.module.ICraftingTimeOverrideModule;
import steve_gall.minecolonies_compatibility.core.common.util.InteractionMessageHelper;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CookingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleCraftingTypes;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizableRecipeStorage;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

public class CookingCraftingModule extends AbstractCraftingModuleWithExternalWorkingBlocks implements ICraftingTimeOverrideModule
{
	public CookingCraftingModule(JobEntry jobEntry)
	{
		super(jobEntry);
	}

	@Override
	public void improveRecipe(IRecipeStorage recipe, int count, ICitizenData citizen)
	{

	}

	@Override
	public boolean isWorkingBlock(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state)
	{
		return level.getBlockEntity(pos) instanceof CookingPotBlockEntity cookingPot && cookingPot.isHeated();
	}

	@Override
	public @NotNull BlockPos getParticlePosition(@NotNull BlockPos pos)
	{
		return super.getParticlePosition(pos).below();
	}

	@Override
	public @NotNull String getId()
	{
		return "farmers_cooking";
	}

	@Override
	public Set<CraftingType> getSupportedCraftingTypes()
	{
		return (this.building == null || this.building.getBuildingLevel() >= 3) ? Collections.singleton(ModuleCraftingTypes.COOKING.get()) : ImmutableSet.of();
	}

	@Override
	public boolean isRecipeCompatible(@NotNull IGenericRecipe recipe)
	{
		return true;
	}

	@Override
	public @NotNull Component getWorkingBlockNotFoundMessage(@NotNull IRecipeStorage recipeStorage)
	{
		return InteractionMessageHelper.getWorkingBlockAndUnderHeatSourceNotFound(recipeStorage.getIntermediate());
	}

	@Override
	public int getCraftingTime(@NotNull AbstractEntityCitizen worker, @NotNull BlockPos workingPos, @NotNull IRecipeStorage recipeStorage)
	{
		if (recipeStorage instanceof ICustomizableRecipeStorage crs && crs.getImpl() instanceof CookingRecipeStorage rs)
		{
			if (worker.getLevel().getRecipeManager().byKey(rs.getRecipeId()).orElse(null) instanceof CookingPotRecipe recipe)
			{
				return recipe.getCookTime();
			}

		}

		return 200;
	}

}
