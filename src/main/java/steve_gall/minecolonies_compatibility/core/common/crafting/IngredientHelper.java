package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import steve_gall.minecolonies_tweaks.api.common.tool.ToolTypeTags;

public class IngredientHelper
{
	private static final Gson GSON = new Gson();

	public static List<List<ItemStack>> getStacksList(@NotNull List<Ingredient> ingredients)
	{
		return ingredients.stream().map(IngredientHelper::getStacks).toList();
	}

	public static List<ItemStack> getStacks(@NotNull Ingredient ingredient)
	{
		return Arrays.asList(ingredient.getItems());
	}

	public static List<Ingredient> filterNotEmpty(List<Ingredient> ingredients)
	{
		return ingredients.stream().filter(ingredient -> !ingredient.isEmpty()).toList();
	}

	public static boolean isTool(@NotNull Ingredient ingredient, @NotNull EquipmentTypeEntry toolType)
	{
		return Arrays.stream(ingredient.getItems()).filter(stack -> !ToolTypeTags.isInBlacklist(stack, toolType.getRegistryName())).allMatch(stack -> ItemStackHelper.isTool(stack, toolType));
	}

	public static @NotNull EquipmentTypeEntry findFirstToolType(@NotNull Ingredient ingredient)
	{
		for (var toolType : IMinecoloniesAPI.getInstance().getEquipmentTypeRegistry().getValues())
		{
			if (toolType == ModEquipmentTypes.none.get())
			{
				continue;
			}
			else if (isTool(ingredient, toolType))
			{
				return toolType;
			}

		}

		return ModEquipmentTypes.none.get();
	}

	public static @NotNull String toJson(@NotNull Ingredient ingredient)
	{
		return GSON.toJson(ingredient.toJson());
	}

	public static @NotNull Ingredient fromJson(@NotNull String json)
	{
		return Ingredient.fromJson(GSON.fromJson(json, JsonElement.class));
	}

	public static boolean isDamageable(@NotNull Ingredient ingredient)
	{
		if (ingredient.isEmpty())
		{
			return false;
		}
		for (var item : ingredient.getItems())
		{
			if (!item.isDamageableItem())
			{
				return false;
			}
		}
		return true;
	}

	private IngredientHelper()
	{

	}

}
