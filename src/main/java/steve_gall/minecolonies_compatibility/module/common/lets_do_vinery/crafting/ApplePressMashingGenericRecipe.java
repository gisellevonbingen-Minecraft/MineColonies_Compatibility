package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting;

import java.util.Collections;
import java.util.List;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.satisfy.vinery.core.recipe.ApplePressMashingRecipe;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleGenericRecipe;

public class ApplePressMashingGenericRecipe extends SimpleGenericRecipe
{
	public ApplePressMashingGenericRecipe(RecipeHolder<ApplePressMashingRecipe> recipe, HolderLookup.Provider provider)
	{
		super(recipe, provider);
	}

	public ApplePressMashingGenericRecipe(ResourceLocation recipeId, List<List<ItemStack>> ingredients, ItemStack output)
	{
		super(recipeId, ingredients, output);
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

}
