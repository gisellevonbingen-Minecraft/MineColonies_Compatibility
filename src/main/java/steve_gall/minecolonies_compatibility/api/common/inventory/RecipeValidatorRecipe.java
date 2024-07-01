package steve_gall.minecolonies_compatibility.api.common.inventory;

import org.jetbrains.annotations.NotNull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;

public abstract class RecipeValidatorRecipe<RECIPE extends Recipe<CONTAINER>, CONTAINER extends Container> implements IRecipeValidator<RECIPE>
{
	public static final String TAG_ID = "id";

	@NotNull
	private final Level level;

	public RecipeValidatorRecipe(@NotNull Level level)
	{
		this.level = level;
	}

	@Override
	public RECIPE test(Level level, ServerPlayer player, TeachRecipeMenu<RECIPE> menu)
	{
		var recipeContainer = this.createRecipeContainer(menu.getCraftMatrix());
		var recipe = level.getRecipeManager().getRecipeFor(this.getRecipeType(), recipeContainer, level).orElse(null);

		if (recipe != null)
		{
			if (recipe.isSpecial() || !level.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING) || player.getRecipeBook().contains(recipe) || player.isCreative())
			{
				return recipe;
			}

		}

		return null;
	}

	@Override
	public CompoundTag serialize(RECIPE recipe)
	{
		var tag = new CompoundTag();
		tag.putString(TAG_ID, recipe.getId().toString());
		return tag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RECIPE deserialize(CompoundTag tag)
	{
		var recipeId = new ResourceLocation(tag.getString(TAG_ID));
		return (RECIPE) this.level.getRecipeManager().byKey(recipeId).orElse(null);
	}

	public abstract RecipeType<RECIPE> getRecipeType();

	public abstract CONTAINER createRecipeContainer(CraftingContainer craftContainer);
}
