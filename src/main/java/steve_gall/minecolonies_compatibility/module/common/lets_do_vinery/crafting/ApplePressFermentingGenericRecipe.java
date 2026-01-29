package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.satisfy.vinery.core.recipe.ApplePressFermentingRecipe;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleContainerGenericRecipe;

public class ApplePressFermentingGenericRecipe extends SimpleContainerGenericRecipe
{
	public ApplePressFermentingGenericRecipe(RecipeHolder<ApplePressFermentingRecipe> recipe, HolderLookup.Provider provider)
	{
		super(recipe, Arrays.asList(getContainer(recipe.value())), provider);
	}

	public ApplePressFermentingGenericRecipe(ResourceLocation recipeId, List<List<ItemStack>> ingredients, List<ItemStack> container, ItemStack output)
	{
		super(recipeId, ingredients, container, output);
	}

	@Override
	public List<ItemStack> getAdditionalOutputs()
	{
		return Collections.emptyList();
	}

	@Override
	public Block getIntermediate()
	{
		return ObjectRegistry.APPLE_PRESS.get();
	}

	public static ItemStack getContainer(ApplePressFermentingRecipe recipe)
	{
		if (recipe.requiresBottle())
		{
			return new ItemStack(ObjectRegistry.WINE_BOTTLE.get());
		}
		else
		{
			return ItemStack.EMPTY;
		}

	}

}
