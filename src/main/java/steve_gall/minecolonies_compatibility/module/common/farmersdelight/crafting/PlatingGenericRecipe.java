package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleGenericRecipe;
import steve_gall.minecolonies_compatibility.core.common.crafting.ItemStorageHelper;

public class PlatingGenericRecipe extends SimpleGenericRecipe
{
	private final PlatingRecipeStorage recipe;

	public PlatingGenericRecipe(PlatingRecipeStorage recipe)
	{
		super(ForgeRegistries.BLOCKS.getKey(recipe.getBlock()), ItemStorageHelper.getStacksLists(recipe.getInput()), recipe.getPrimaryOutput());
		this.recipe = recipe;
	}

	public PlatingRecipeStorage getRecipeStorage()
	{
		return this.recipe;
	}

}
