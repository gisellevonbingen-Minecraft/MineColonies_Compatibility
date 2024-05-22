package steve_gall.minecolonies_compatibility.module.client.jei;

import mezz.jei.api.recipe.RecipeType;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.core.common.init.ModJobs;

public class ModJeiRecipeTypes
{
	public static final RecipeType<CustomizedFruit> ORCHARDIST_FRUIT = new RecipeType<>(ModJobs.ORCHARDIST.getId(), CustomizedFruit.class);

	private ModJeiRecipeTypes()
	{

	}

}
