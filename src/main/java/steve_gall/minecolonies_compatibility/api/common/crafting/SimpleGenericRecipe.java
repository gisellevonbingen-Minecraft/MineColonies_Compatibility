package steve_gall.minecolonies_compatibility.api.common.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;
import com.minecolonies.api.util.OptionalPredicate;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import steve_gall.minecolonies_compatibility.core.common.crafting.GenericRecipeHelper;
import steve_gall.minecolonies_compatibility.core.common.crafting.IngredientHelper;

public class SimpleGenericRecipe implements IGenericRecipe
{
	@NotNull
	private final ResourceLocation recipeId;
	@NotNull
	private final List<List<ItemStack>> input;
	@NotNull
	private final ItemStack output;
	@NotNull
	private final List<ItemStack> additionalOutputs;

	public SimpleGenericRecipe(@NotNull Recipe<?> recipe, @NotNull RegistryAccess registryAccess)
	{
		this(recipe.getId(), IngredientHelper.getStacksList(recipe.getIngredients()), recipe.getResultItem(registryAccess));
	}

	public SimpleGenericRecipe(@NotNull ResourceLocation recipeId, @NotNull List<List<ItemStack>> ingredients, @NotNull ItemStack output)
	{
		this.recipeId = recipeId;
		this.input = new ArrayList<>(ingredients);
		this.output = output;
		this.additionalOutputs = GenericRecipeHelper.getAdditionalOutputs(ingredients);
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
		return this.additionalOutputs;
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
		return Blocks.AIR;
	}

	@Override
	public @Nullable ResourceLocation getLootTable()
	{
		return null;
	}

	@Override
	public @NotNull EquipmentTypeEntry getRequiredTool()
	{
		return ModEquipmentTypes.none.get();
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
