package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.block.FeastBlock;

public class PlatingCraftingType extends CraftingType
{
	public PlatingCraftingType(@NotNull ResourceLocation id)
	{
		super(id);
	}

	@Override
	public @NotNull List<IGenericRecipe> findRecipes(@NotNull RecipeManager recipeManager, @Nullable Level world)
	{
		var recipes = new ArrayList<IGenericRecipe>();

		for (var block : BuiltInRegistries.BLOCK)
		{
			if (block instanceof FeastBlock feastBlock)
			{
				var recipe = new PlatingRecipeStorage(feastBlock);

				if (!recipe.getPrimaryOutput().isEmpty())
				{
					recipes.add(recipe.getGenericRecipe());
				}

			}

		}

		return recipes;
	}

}
