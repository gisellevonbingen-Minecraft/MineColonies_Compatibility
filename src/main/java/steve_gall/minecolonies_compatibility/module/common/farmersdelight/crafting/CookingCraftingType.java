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
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class CookingCraftingType extends RecipeCraftingType<RecipeWrapper, CookingPotRecipe>
{
	public CookingCraftingType(@NotNull ResourceLocation id)
	{
		super(id, null, null);
	}

	@Override
	public @NotNull List<IGenericRecipe> findRecipes(@NotNull RecipeManager recipeManager, @Nullable Level world)
	{
		var recipes = new ArrayList<IGenericRecipe>();
		var registryAccess = world.registryAccess();

		for (var recipe : recipeManager.getAllRecipesFor(ModRecipeTypes.COOKING.get()))
		{
			recipes.add(new CookingGenericRecipe(recipe, registryAccess));
		}

		return recipes;
	}

}
