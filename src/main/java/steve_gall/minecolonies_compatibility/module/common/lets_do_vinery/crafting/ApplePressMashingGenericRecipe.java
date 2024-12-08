package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.satisfy.vinery.core.recipe.ApplePressMashingRecipe;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleGenericRecipe;

public class ApplePressMashingGenericRecipe extends SimpleGenericRecipe
{
	public ApplePressMashingGenericRecipe(@NotNull ApplePressMashingRecipe recipe, @NotNull RegistryAccess registryAccess)
	{
		super(recipe, registryAccess);
	}

	public ApplePressMashingGenericRecipe(@NotNull ResourceLocation recipeId, @NotNull List<List<ItemStack>> ingredients, @NotNull ItemStack output)
	{
		super(recipeId, ingredients, output);
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

}
