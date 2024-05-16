package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class IngredientHelper
{
	public static List<List<ItemStack>> getStacksList(@NotNull List<Ingredient> ingredients)
	{
		return ingredients.stream().map(IngredientHelper::getStacks).toList();
	}

	public static List<ItemStack> getStacks(@NotNull Ingredient ingredient)
	{
		return Arrays.asList(ingredient.getItems());
	}

	private IngredientHelper()
	{

	}

}
