package steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.crafting;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.satisfy.meadow.core.registry.ObjectRegistry;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleGenericRecipe;

public class CheeseGenericRecipe extends SimpleGenericRecipe
{
	public CheeseGenericRecipe(@NotNull Recipe<?> recipe, @NotNull RegistryAccess registryAccess)
	{
		super(recipe, registryAccess);
	}

	public CheeseGenericRecipe(@NotNull ResourceLocation recipeId, @NotNull List<List<ItemStack>> ingredients, @NotNull ItemStack output)
	{
		super(recipeId, ingredients, output);
	}

	@Override
	public @NotNull Block getIntermediate()
	{
		return ObjectRegistry.CHEESE_FORM.get();
	}

}
