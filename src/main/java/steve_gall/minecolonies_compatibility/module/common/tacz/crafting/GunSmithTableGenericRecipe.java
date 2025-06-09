package steve_gall.minecolonies_compatibility.module.common.tacz.crafting;

import java.util.List;

import com.tacz.guns.crafting.GunSmithTableRecipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleGenericRecipe;
import steve_gall.minecolonies_compatibility.core.common.crafting.IngredientHelper;

public class GunSmithTableGenericRecipe extends SimpleGenericRecipe
{
	public GunSmithTableGenericRecipe(GunSmithTableRecipe recipe)
	{
		this(recipe.getId(), recipe.getInputs().stream().map(input ->
		{
			var stacks = IngredientHelper.getStacks(input.getIngredient());

			for (var i = 0; i < stacks.size(); i++)
			{
				var stack = stacks.get(i).copy();
				stack.setCount(input.getCount());
				stacks.set(i, stack);
			}

			return stacks;
		}).toList(), recipe.getResultItem());
	}

	public GunSmithTableGenericRecipe(ResourceLocation recipeId, List<List<ItemStack>> ingredients, ItemStack output)
	{
		super(recipeId, ingredients, output);
	}

}
