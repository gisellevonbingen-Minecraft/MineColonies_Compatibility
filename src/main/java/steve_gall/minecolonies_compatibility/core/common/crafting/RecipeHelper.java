package steve_gall.minecolonies_compatibility.core.common.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import steve_gall.minecolonies_compatibility.core.common.inventory.ContainerHelper;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;

public class RecipeHelper
{
	public static boolean matchesIngredientCount(Recipe<?> recipe, Container container)
	{
		var ingredientsSize = recipe.getIngredients().size();
		var inputsSize = ItemStackHelper.filterNotEmpty(ContainerHelper.getItemStacks(container)).size();
		return ingredientsSize == inputsSize;
	}

	private RecipeHelper()
	{

	}

}
