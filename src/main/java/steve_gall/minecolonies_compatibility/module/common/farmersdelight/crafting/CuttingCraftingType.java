package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.RecipeCraftingType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_compatibility.core.common.crafting.IngredientHelper;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipeInput;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class CuttingCraftingType extends RecipeCraftingType<CuttingBoardRecipeInput, CuttingBoardRecipe>
{
	public CuttingCraftingType(@NotNull ResourceLocation id)
	{
		super(id, null, null);
	}

	@Override
	public @NotNull List<IGenericRecipe> findRecipes(@NotNull RecipeManager recipeManager, @Nullable Level world)
	{
		var recipes = new ArrayList<IGenericRecipe>();

		for (var recipe : recipeManager.getAllRecipesFor(ModRecipeTypes.CUTTING.get()))
		{
			var toolType = IngredientHelper.findFirstToolType(recipe.value().getTool());
			recipes.add(new CuttingGenericRecipe(recipe, toolType));
		}

		return recipes;
	}

}
