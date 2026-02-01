package steve_gall.minecolonies_compatibility.api.common.inventory;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_compatibility.core.common.inventory.ContainerHelper;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;

public abstract class MenuRecipeValidatorRecipe<RECIPE extends Recipe<CONTAINER>, CONTAINER extends Container> implements IMenuRecipeValidator<RECIPE>
{
	public static final String TAG_ID = "id";

	@NotNull
	protected final Level level;

	public MenuRecipeValidatorRecipe(@NotNull Level level)
	{
		this.level = level;
	}

	@Override
	public List<RECIPE> findAll(Container container, ServerPlayer player)
	{
		return this.level.getRecipeManager().getAllRecipesFor(this.getRecipeType()).stream().filter(recipe ->
		{
			if (this.test(recipe, container, player))
			{
				return recipe.isSpecial() || !this.level.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING) || player.getRecipeBook().contains(recipe) || player.isCreative();
			}

			return false;
		}).toList();
	}

	protected abstract boolean test(RECIPE recipe, Container container, ServerPlayer player);

	protected boolean matchesWithIngredientsCount(RECIPE recipe, CONTAINER container)
	{
		if (!recipe.matches(container, this.level))
		{
			return false;
		}

		var ingredientsSize = recipe.getIngredients().size();
		var inputsSize = ItemStackHelper.filterNotEmpty(ContainerHelper.getItemStacks(container)).size();
		return ingredientsSize == inputsSize;
	}

	@Override
	public CompoundTag serialize(IFactoryController controller, RECIPE recipe)
	{
		var tag = new CompoundTag();
		tag.putString(TAG_ID, recipe.getId().toString());
		return tag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RECIPE deserialize(IFactoryController controller, CompoundTag tag)
	{
		var recipeId = new ResourceLocation(tag.getString(TAG_ID));
		return (RECIPE) this.level.getRecipeManager().byKey(recipeId).orElse(null);
	}

	@Override
	public ItemStack getResultItem(RECIPE recipe, RegistryAccess registryAccess)
	{
		return recipe.getResultItem(registryAccess);
	}

	@Override
	public ResourceLocation getRecipeId(RECIPE recipe)
	{
		return recipe.getId();
	}

	public abstract RecipeType<RECIPE> getRecipeType();
}
