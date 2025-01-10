package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

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

	public static boolean isTool(@NotNull Ingredient ingredient, @NotNull EquipmentTypeEntry toolType)
	{
		return Arrays.stream(ingredient.getItems()).allMatch(stack -> ItemStackHelper.isTool(stack, toolType));
	}

	public static @NotNull EquipmentTypeEntry findFirstToolType(@NotNull Ingredient ingredient)
	{
		for (var toolType : IMinecoloniesAPI.getInstance().getEquipmentTypeRegistry().getValues())
		{
			if (isTool(ingredient, toolType))
			{
				return toolType;
			}

		}

		return ModEquipmentTypes.none.get();
	}

	private IngredientHelper()
	{

	}

}
