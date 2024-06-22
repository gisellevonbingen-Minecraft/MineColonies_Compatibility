package steve_gall.minecolonies_compatibility.module.client.jei;

import mezz.jei.api.recipe.RecipeType;
import steve_gall.minecolonies_compatibility.api.common.plant.FruitIconCache;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.init.ModJobs;
import steve_gall.minecolonies_compatibility.module.client.jei.ResearchCategory.ResearchCache;

public class ModJeiRecipeTypes
{
	public static final RecipeType<ResearchCache> RESEARCH = new RecipeType<>(MineColoniesCompatibility.rl("research"), ResearchCache.class);

	public static final RecipeType<FruitIconCache> ORCHARDIST_FRUIT = new RecipeType<>(ModJobs.ORCHARDIST.getId(), FruitIconCache.class);

	private ModJeiRecipeTypes()
	{

	}

}
