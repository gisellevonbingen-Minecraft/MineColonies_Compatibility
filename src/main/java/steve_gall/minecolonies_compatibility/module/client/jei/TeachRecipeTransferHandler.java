package steve_gall.minecolonies_compatibility.module.client.jei;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.crafting.ItemStorage;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.network.message.JEIRecipeTransferMessage;

public abstract class TeachRecipeTransferHandler<MENU extends TeachRecipeMenu<RECIPE>, RECIPE, CATEGORY_RECIPE> implements IRecipeTransferHandler<MENU, CATEGORY_RECIPE>
{
	private final IRecipeTransferHandlerHelper recipeTransferHandlerHelper;

	public TeachRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper)
	{
		this.recipeTransferHandlerHelper = recipeTransferHandlerHelper;
	}

	@Override
	public @Nullable IRecipeTransferError transferRecipe(MENU menu, CATEGORY_RECIPE categoryRecipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer)
	{
		var recipe = this.getRecipe(menu, categoryRecipe, recipeSlots, player);

		if (recipe == null)
		{
			return this.recipeTransferHandlerHelper.createInternalError();
		}

		var error = this.getError(menu, recipe, recipeSlots, player);

		if (error != null)
		{
			return this.recipeTransferHandlerHelper.createUserErrorWithTooltip(error);
		}

		if (doTransfer)
		{
			var tag = new CompoundTag();
			this.serializePayload(menu, recipe, recipeSlots, player, tag);
			MineColoniesCompatibility.network().sendToServer(new JEIRecipeTransferMessage<>(menu, recipe, tag));
		}

		return null;
	}

	protected Component getError(MENU menu, RECIPE recipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return menu.getRecipeError(recipe);
	}

	protected List<ItemStack> getDisplayedItemStacks(IRecipeSlotsView recipeSlots, RecipeIngredientRole role)
	{
		return recipeSlots.getSlotViews(role).stream().map(view -> view.getDisplayedIngredient(VanillaTypes.ITEM_STACK).orElse(ItemStack.EMPTY)).toList();
	}

	protected List<List<ItemStack>> getItemStacksList(IRecipeSlotsView recipeSlots, RecipeIngredientRole role)
	{
		return recipeSlots.getSlotViews(role).stream().map(view -> view.getIngredients(VanillaTypes.ITEM_STACK).toList()).toList();
	}

	protected List<ItemStorage> getDisplayedItemStorages(IRecipeSlotsView recipeSlots, RecipeIngredientRole role)
	{
		return this.getDisplayedItemStacks(recipeSlots, role).stream().map(stack -> new ItemStorage(ItemHandlerHelper.copyStackWithSize(stack, 1), stack.getCount(), false)).toList();
	}

	protected List<List<ItemStorage>> getItemStoragesList(IRecipeSlotsView recipeSlots, RecipeIngredientRole role)
	{
		return this.getItemStacksList(recipeSlots, role).stream().map(l -> l.stream().map(stack -> new ItemStorage(ItemHandlerHelper.copyStackWithSize(stack, 1), stack.getCount(), false)).toList()).toList();
	}

	protected abstract RECIPE getRecipe(MENU menu, CATEGORY_RECIPE categoryRecipe, IRecipeSlotsView recipeSlots, Player player);

	protected abstract void serializePayload(MENU menu, RECIPE recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag);
}
