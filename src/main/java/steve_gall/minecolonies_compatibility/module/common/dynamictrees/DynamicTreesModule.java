package steve_gall.minecolonies_compatibility.module.common.dynamictrees;

import com.dtteam.dynamictrees.tree.species.Species;

import net.minecraft.world.item.crafting.RecipeManager;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class DynamicTreesModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();
	}

	@Override
	protected void onRecipeReloaded(RecipeManager recipeManager)
	{
		super.onRecipeReloaded(recipeManager);

		for (var species : Species.REGISTRY)
		{
			var seed = species.getSeed().orElse(null);
			var fruits = species.getFruits();

			if (seed == null || fruits.size() == 0)
			{
				continue;
			}

			CustomizedFruit.registerVolatile(new SpeciesFruit(species, seed, fruits));
		}

	}

}
