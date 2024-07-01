package steve_gall.minecolonies_compatibility.api.common.inventory;

import org.jetbrains.annotations.NotNull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public abstract class MenuRecipeValidatorRecipe<RECIPE extends Recipe<CONTAINER>, CONTAINER extends Container> implements IMenuRecipeValidator<RECIPE>
{
	public static final String TAG_ID = "id";

	@NotNull
	private final Level level;

	public MenuRecipeValidatorRecipe(@NotNull Level level)
	{
		this.level = level;
	}

	@Override
	public RECIPE find(ServerPlayer player, Container container)
	{
		var recipeContainer = this.createRecipeContainer(container);
		var recipe = this.level.getRecipeManager().getRecipeFor(this.getRecipeType(), recipeContainer, this.level).orElse(null);

		if (recipe != null)
		{
			if (recipe.isSpecial() || !this.level.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING) || player.getRecipeBook().contains(recipe) || player.isCreative())
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

	public abstract CONTAINER createRecipeContainer(Container container);
}
