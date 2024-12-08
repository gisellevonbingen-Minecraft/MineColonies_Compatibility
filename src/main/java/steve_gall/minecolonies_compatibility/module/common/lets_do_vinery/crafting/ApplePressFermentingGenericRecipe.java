package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.satisfy.vinery.core.recipe.ApplePressFermentingRecipe;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleContainerGenericRecipe;

public class ApplePressFermentingGenericRecipe extends SimpleContainerGenericRecipe
{
	public ApplePressFermentingGenericRecipe(@NotNull ApplePressFermentingRecipe recipe, @NotNull RegistryAccess registryAccess)
	{
		super(recipe, Arrays.asList(getContainer(recipe)), registryAccess);
	}

	public ApplePressFermentingGenericRecipe(@NotNull ResourceLocation recipeId, @NotNull List<List<ItemStack>> ingredients, List<ItemStack> container, @NotNull ItemStack output)
	{
		super(recipeId, ingredients, container, output);
	}

	@Override
	public @NotNull List<ItemStack> getAdditionalOutputs()
	{
		return Collections.emptyList();
	}

	@Override
	public @NotNull Block getIntermediate()
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
