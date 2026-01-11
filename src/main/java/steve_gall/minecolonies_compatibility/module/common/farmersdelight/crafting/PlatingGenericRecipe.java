package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import net.minecraft.core.registries.BuiltInRegistries;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleGenericRecipe;
import steve_gall.minecolonies_compatibility.core.common.crafting.ItemStorageHelper;

public class PlatingGenericRecipe extends SimpleGenericRecipe
{
	private final PlatingRecipeStorage recipe;

	public PlatingGenericRecipe(PlatingRecipeStorage recipe)
	{
		super(BuiltInRegistries.BLOCK.getKey(recipe.getBlock()), ItemStorageHelper.getAmountedStacksLists(recipe.getInput()), recipe.getPrimaryOutput());
		this.recipe = recipe;
	}

	public PlatingRecipeStorage getRecipeStorage()
	{
		return this.recipe;
	}

}
