package steve_gall.minecolonies_compatibility.module.common.butchercraft.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.lance5057.butchercraft.ButchercraftBlocks;
import com.lance5057.butchercraft.workstations.grinder.GrinderRecipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleGenericRecipe;

public class GrinderGenericRecipe extends SimpleGenericRecipe
{
	private final List<ItemStack> attachment;

	public GrinderGenericRecipe(RecipeHolder<GrinderRecipe> recipe, RegistryAccess registryAccess)
	{
		this(recipe.id(), Collections.singletonList(ingredients(recipe.value().ingredient(), recipe.value().count())), recipe.value().getResultItem(registryAccess), recipe.value().attachment());
	}

	public GrinderGenericRecipe(ResourceLocation recipeId, List<List<ItemStack>> ingredients, ItemStack output, Ingredient attachment)
	{
		super(recipeId, ingredients, output);

		this.attachment = Arrays.asList(attachment.getItems());
	}

	public static List<ItemStack> ingredients(Ingredient ingredient, int count)
	{
		return Arrays.stream(ingredient.getItems()).map(item ->
		{
			var copy = item.copy();
			copy.setCount(count);
			return copy;
		}).toList();
	}

	@Override
	public @NotNull List<List<ItemStack>> getInputs()
	{
		var list = new ArrayList<List<ItemStack>>();
		list.addAll(super.getInputs());

		if (this.attachment.size() > 0)
		{
			list.add(this.attachment);
		}

		return list;
	}

	@Override
	public @NotNull Block getIntermediate()
	{
		return ButchercraftBlocks.GRINDER.get();
	}

	public List<ItemStack> getAttachment()
	{
		return this.attachment;
	}

}
