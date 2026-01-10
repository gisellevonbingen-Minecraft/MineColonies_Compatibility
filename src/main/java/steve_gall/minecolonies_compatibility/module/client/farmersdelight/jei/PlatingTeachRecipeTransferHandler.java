package steve_gall.minecolonies_compatibility.module.client.farmersdelight.jei;

import java.util.Optional;

import com.minecolonies.api.crafting.IGenericRecipe;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import steve_gall.minecolonies_compatibility.module.client.jei.TeachRecipeTransferHandler;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.PlatingGenericRecipe;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.PlatingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.PlatingTeachMenu;

public class PlatingTeachRecipeTransferHandler extends TeachRecipeTransferHandler<PlatingTeachMenu, PlatingRecipeStorage, IGenericRecipe>
{
	private final RecipeType<IGenericRecipe> recipeType;

	public PlatingTeachRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper, RecipeType<IGenericRecipe> recipeType)
	{
		super(recipeTransferHandlerHelper);
		this.recipeType = recipeType;
	}

	@Override
	public Class<? extends PlatingTeachMenu> getContainerClass()
	{
		return PlatingTeachMenu.class;
	}

	@Override
	public Optional<MenuType<PlatingTeachMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<IGenericRecipe> getRecipeType()
	{
		return this.recipeType;
	}

	@Override
	protected PlatingRecipeStorage getRecipe(PlatingTeachMenu menu, IGenericRecipe categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		if (categoryRecipe instanceof PlatingGenericRecipe genericRecipe)
		{
			return genericRecipe.getRecipeStorage();
		}
		else
		{
			return null;
		}

	}

	@Override
	protected void serializePayload(PlatingTeachMenu menu, PlatingRecipeStorage recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		tag.put("input", input.get(0).serializeNBT());
	}

}
