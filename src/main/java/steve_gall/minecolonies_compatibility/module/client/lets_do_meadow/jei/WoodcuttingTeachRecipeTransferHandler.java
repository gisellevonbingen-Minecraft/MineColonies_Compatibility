package steve_gall.minecolonies_compatibility.module.client.lets_do_meadow.jei;

import java.util.Optional;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.satisfy.meadow.core.compat.jei.category.WoodCutterCategory;
import net.satisfy.meadow.core.recipes.WoodcuttingRecipe;
import steve_gall.minecolonies_compatibility.module.client.jei.TeachRecipeTransferHandler;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.menu.WoodcuttingTeachMenu;

public class WoodcuttingTeachRecipeTransferHandler extends TeachRecipeTransferHandler<WoodcuttingTeachMenu, WoodcuttingRecipe, WoodcuttingRecipe>
{
	public WoodcuttingTeachRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper)
	{
		super(recipeTransferHandlerHelper);
	}

	@Override
	public Class<? extends WoodcuttingTeachMenu> getContainerClass()
	{
		return WoodcuttingTeachMenu.class;
	}

	@Override
	public Optional<MenuType<WoodcuttingTeachMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<WoodcuttingRecipe> getRecipeType()
	{
		return WoodCutterCategory.WOODCUTTER;
	}

	@Override
	protected WoodcuttingRecipe getRecipe(WoodcuttingTeachMenu menu, WoodcuttingRecipe categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return categoryRecipe;
	}

	@Override
	protected void serializePayload(WoodcuttingTeachMenu menu, WoodcuttingRecipe recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		tag.put("input", input.get(0).serializeNBT());
	}

}
