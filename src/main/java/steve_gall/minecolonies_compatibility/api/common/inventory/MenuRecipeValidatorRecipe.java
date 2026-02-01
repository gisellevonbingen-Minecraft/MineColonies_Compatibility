package steve_gall.minecolonies_compatibility.api.common.inventory;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;

public abstract class MenuRecipeValidatorRecipe<RECIPE extends Recipe<RECIPE_INPUT>, RECIPE_INPUT extends RecipeInput> implements IMenuRecipeValidator<RecipeHolder<RECIPE>, RECIPE_INPUT>
{
	public static final String TAG_ID = "id";

	@NotNull
	protected final Level level;

	public MenuRecipeValidatorRecipe(@NotNull Level level)
	{
		this.level = level;
	}

	@Override
	public List<RecipeHolder<RECIPE>> findAll(Container container, ServerPlayer player)
	{
		return this.level.getRecipeManager().getAllRecipesFor(this.getRecipeType()).stream().filter(recipeHolder ->
		{
			var recipe = recipeHolder.value();

			if (this.test(recipeHolder, container, player))
			{
				return recipe.isSpecial() || !this.level.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING) || player.getRecipeBook().contains(recipeHolder) || player.isCreative();
			}

			return false;
		}).toList();
	}

	protected boolean test(RecipeHolder<RECIPE> recipeHolder, Container container, ServerPlayer player)
	{
		var input = this.getInput(container, recipeHolder);
		return recipeHolder.value().matches(input, player.level());
	}

	protected boolean matchesWithIngredientsCount(RECIPE recipe, RECIPE_INPUT input)
	{
		if (!recipe.matches(input, this.level))
		{
			return false;
		}

		var ingredientsSize = recipe.getIngredients().size();
		var inputsSize = ItemStackHelper.filterNotEmpty(RecipeInputHelper.getItemStacks(input)).size();
		return ingredientsSize == inputsSize;
	}

	@Override
	public CompoundTag serialize(HolderLookup.Provider provider, IFactoryController controller, RecipeHolder<RECIPE> recipeHolder)
	{
		var tag = new CompoundTag();
		tag.putString(TAG_ID, recipeHolder.id().toString());
		return tag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RecipeHolder<RECIPE> deserialize(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag)
	{
		var recipeId = ResourceLocation.parse(tag.getString(TAG_ID));
		return (RecipeHolder<RECIPE>) this.level.getRecipeManager().byKey(recipeId).orElse(null);
	}

	@Override
	public ItemStack getResultItem(RecipeHolder<RECIPE> recipe, HolderLookup.Provider provider)
	{
		return recipe.value().getResultItem(provider);
	}

	@Override
	public ResourceLocation getRecipeId(RecipeHolder<RECIPE> recipe)
	{
		return recipe.id();
	}

	public abstract RecipeType<RECIPE> getRecipeType();
}
