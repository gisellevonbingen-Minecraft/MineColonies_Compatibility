package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;

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

	public static boolean isTool(@NotNull Ingredient ingredient, @NotNull IToolType toolType)
	{
		return Arrays.stream(ingredient.getItems()).allMatch(stack -> ItemStackHelper.isTool(stack, toolType));
	}

	public static @NotNull ToolType findFirstToolType(@NotNull Ingredient ingredient)
	{
		for (var toolType : ToolType.values())
		{
			if (isTool(ingredient, toolType))
			{
				return toolType;
			}

		}

		return ToolType.NONE;
	}

	private IngredientHelper()
	{

	}

}
