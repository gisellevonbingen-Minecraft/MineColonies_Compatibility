package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.util.OptionalPredicate;
import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.core.common.crafting.IngredientHelper;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class CookingGenericRecipe implements IGenericRecipe
{
	private final ResourceLocation recipeId;
	private final List<List<ItemStack>> input;
	private final ItemStack output;

	public CookingGenericRecipe(CookingPotRecipe recipe, RegistryAccess registryAccess)
	{
		this(recipe.getId(), IngredientHelper.getStacksList(recipe.getIngredients()), Collections.singletonList(recipe.getOutputContainer()), recipe.getResultItem(registryAccess));
	}

	public CookingGenericRecipe(ResourceLocation recipeId, List<List<ItemStack>> ingredients, List<ItemStack> container, ItemStack output)
	{
		this.recipeId = recipeId;
		this.input = new ArrayList<>(ingredients);

		if (!container.isEmpty() && !container.stream().allMatch(ItemStack::isEmpty))
		{
			this.input.add(container);
		}

		this.output = output;
	}

	@Override
	public int getGridSize()
	{
		return 1;
	}

	@Override
	public @Nullable ResourceLocation getRecipeId()
	{
		return this.recipeId;
	}

	@Override
	public @NotNull ItemStack getPrimaryOutput()
	{
		return this.output;
	}

	@Override
	public @NotNull List<ItemStack> getAllMultiOutputs()
	{
		return Collections.singletonList(this.output);
	}

	@Override
	public @NotNull List<ItemStack> getAdditionalOutputs()
	{
		return Collections.emptyList();
	}

	@Override
	public @NotNull List<List<ItemStack>> getInputs()
	{
		return this.input;
	}

	@Override
	public Optional<Boolean> matchesOutput(@NotNull OptionalPredicate<ItemStack> predicate)
	{
		return Optional.empty();
	}

	@Override
	public Optional<Boolean> matchesInput(@NotNull OptionalPredicate<ItemStack> predicate)
	{
		return Optional.empty();
	}

	@Override
	public @NotNull Block getIntermediate()
	{
		return ModBlocks.COOKING_POT.get();
	}

	@Override
	public @Nullable ResourceLocation getLootTable()
	{
		return null;
	}

	@Override
	public @NotNull IToolType getRequiredTool()
	{
		return ToolType.NONE;
	}

	@Override
	public @Nullable LivingEntity getRequiredEntity()
	{
		return null;
	}

	@Override
	public @NotNull List<Component> getRestrictions()
	{
		return Collections.emptyList();
	}

	@Override
	public int getLevelSort()
	{
		return -1;
	}

}
