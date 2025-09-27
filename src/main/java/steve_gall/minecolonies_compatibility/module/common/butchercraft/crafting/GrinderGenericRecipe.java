package steve_gall.minecolonies_compatibility.module.common.butchercraft.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.lance5057.butchercraft.ButchercraftBlocks;
import com.lance5057.butchercraft.ButchercraftItems;
import com.lance5057.butchercraft.tags.ButchercraftItemTags;
import com.lance5057.butchercraft.workstations.grinder.GrinderRecipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleGenericRecipe;

public class GrinderGenericRecipe extends SimpleGenericRecipe
{
	private final List<ItemStack> attachment;

	public GrinderGenericRecipe(GrinderRecipe recipe, RegistryAccess registryAccess)
	{
		this(recipe.getId(), ingredientsWithCasing(recipe), recipe.getResultItem(registryAccess), recipe.attachment);
	}

	public GrinderGenericRecipe(ResourceLocation recipeId, List<List<ItemStack>> ingredients, ItemStack output, Ingredient attachment)
	{
		super(recipeId, ingredients, output);

		this.attachment = Arrays.asList(attachment.getItems());
	}

	public static List<List<ItemStack>> ingredientsWithCasing(GrinderRecipe recipe)
	{
		var list = new ArrayList<List<ItemStack>>();
		list.add(ingredients(recipe.ingredient, recipe.count));

		var casing = casing(recipe.attachment);

		if (casing.size() > 0)
		{
			list.add(casing);
		}

		return list;
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

	public static List<ItemStack> casing(Ingredient attachment)
	{
		return Arrays.stream(attachment.getItems()).map(GrinderGenericRecipe::casing).flatMap(i -> Arrays.stream(i.getItems())).toList();
	}

	public static Ingredient casing(ItemStack attachment)
	{
		if (attachment.is(ButchercraftItems.EXTRUDER_TIP.get()))
		{
			return Ingredient.of(ButchercraftItemTags.SAUSAGE_CASING);
		}
		else
		{
			return Ingredient.EMPTY;
		}

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
