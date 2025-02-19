package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

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

	public static boolean isTool(@NotNull Ingredient ingredient, @NotNull IToolType toolType)
	{
		return Arrays.stream(ingredient.getItems()).filter(stack -> !ToolTypeTags.isInBlacklist(stack, toolType.getName())).allMatch(stack -> ItemStackHelper.isTool(stack, toolType));
	}

	public static @NotNull ToolType findFirstToolType(@NotNull Ingredient ingredient)
	{
		for (var toolType : ToolType.values())
		{
			if (toolType == ToolType.NONE)
			{
				continue;
			}
			else if (isTool(ingredient, toolType))
			{
				return toolType;
			}

		}

		return ToolType.NONE;
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
