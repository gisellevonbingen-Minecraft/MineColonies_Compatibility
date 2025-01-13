package steve_gall.minecolonies_compatibility.module.common.butchercraft.crafting;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.lance5057.butchercraft.ButchercraftRecipes;
import com.lance5057.butchercraft.workstations.grinder.GrinderContainer;
import com.lance5057.butchercraft.workstations.grinder.GrinderRecipe;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.RecipeCraftingType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

public class GrinderCraftingType extends RecipeCraftingType<GrinderContainer, GrinderRecipe>
{
	public GrinderCraftingType(@NotNull ResourceLocation id)
	{
		super(id, null, null);
	}

	@Override
	public @NotNull List<IGenericRecipe> findRecipes(@NotNull RecipeManager recipeManager, @Nullable Level world)
	{
		var recipes = new ArrayList<IGenericRecipe>();

		for (var recipe : recipeManager.getAllRecipesFor(ButchercraftRecipes.GRINDER.get()))
		{
			recipes.add(new GrinderGenericRecipe(recipe));
		}

		return recipes;
	}

}
