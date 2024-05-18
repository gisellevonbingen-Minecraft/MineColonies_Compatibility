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
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.crafting.IngredientHelper;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import steve_gall.minecolonies_compatibility.core.common.network.message.JEIRecipeTransferMessage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.TeachCuttingMenu;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;

public class TeachCuttingRecipeTransferHandler implements IRecipeTransferHandler<TeachCuttingMenu, CuttingBoardRecipe>
{
	private final IRecipeTransferHandlerHelper recipeTransferHandlerHelper;

	public TeachCuttingRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper)
	{
		this.recipeTransferHandlerHelper = recipeTransferHandlerHelper;
	}

	@Override
	public Class<? extends TeachCuttingMenu> getContainerClass()
	{
		return TeachCuttingMenu.class;
	}

	@Override
	public Optional<MenuType<TeachCuttingMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<CuttingBoardRecipe> getRecipeType()
	{
		return FDRecipeTypes.CUTTING;
	}

	@Override
	@Nullable
	public IRecipeTransferError transferRecipe(TeachCuttingMenu menu, CuttingBoardRecipe recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer)
	{
		if (!ItemStackHelper.isTool(IngredientHelper.getStacks(recipe.getTool()), menu.getToolType()))
		{
			return this.recipeTransferHandlerHelper.createUserErrorWithTooltip(Component.translatable("minecolonies_compatibility.jei.unsupported_tool"));
		}

		if (doTransfer)
		{
			var input = recipeSlots.getSlotViews(RecipeIngredientRole.INPUT).get(1).getDisplayedIngredient(VanillaTypes.ITEM_STACK).orElse(ItemStack.EMPTY);
			MineColoniesCompatibility.network().sendToServer(new JEIRecipeTransferMessage(recipe, input.serializeNBT()));
		}

		return null;
	}

}
