package steve_gall.minecolonies_compatibility.module.client.farmersdelight.jei;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.JEIRecipeTransferMessage;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.TeachCookingMenu;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;

public class TeachCookingRecipeTransferHandler implements IRecipeTransferHandler<TeachCookingMenu, CookingPotRecipe>
{
	private final IRecipeTransferHandlerHelper recipeTransferHandlerHelper;

	public TeachCookingRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper)
	{
		this.recipeTransferHandlerHelper = recipeTransferHandlerHelper;
	}

	@Override
	public Class<? extends TeachCookingMenu> getContainerClass()
	{
		return TeachCookingMenu.class;
	}

	@Override
	public Optional<MenuType<TeachCookingMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<CookingPotRecipe> getRecipeType()
	{
		return FDRecipeTypes.COOKING;
	}

	@Override
	@Nullable
	public IRecipeTransferError transferRecipe(TeachCookingMenu menu, CookingPotRecipe recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer)
	{
		if (doTransfer)
		{
			var tag = new CompoundTag();
			var input = recipeSlots.getSlotViews(RecipeIngredientRole.INPUT).stream().map(view -> view.getDisplayedIngredient(VanillaTypes.ITEM_STACK).orElse(ItemStack.EMPTY)).toList();
			NBTUtils2.serializeList(tag, "input", input, ItemStack::serializeNBT);
			MineColoniesCompatibility.network().sendToServer(new JEIRecipeTransferMessage(recipe, tag));
		}

		return null;
	}

}
