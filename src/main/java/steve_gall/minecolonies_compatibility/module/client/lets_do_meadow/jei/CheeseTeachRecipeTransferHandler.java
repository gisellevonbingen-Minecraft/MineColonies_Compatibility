package steve_gall.minecolonies_compatibility.module.client.lets_do_meadow.jei;

import java.util.Optional;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.satisfy.meadow.core.compat.jei.category.CheesePressCategory;
import net.satisfy.meadow.core.recipes.CheeseFormRecipe;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.client.jei.TeachRecipeTransferHandler;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.menu.CheeseTeachMenu;

public class CheeseTeachRecipeTransferHandler extends TeachRecipeTransferHandler<CheeseTeachMenu, CheeseFormRecipe, CheeseFormRecipe>
{
	public CheeseTeachRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper)
	{
		super(recipeTransferHandlerHelper);
	}

	@Override
	public Class<? extends CheeseTeachMenu> getContainerClass()
	{
		return CheeseTeachMenu.class;
	}

	@Override
	public Optional<MenuType<CheeseTeachMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<CheeseFormRecipe> getRecipeType()
	{
		return CheesePressCategory.CHEESE_PRESS;
	}

	@Override
	protected CheeseFormRecipe getRecipe(CheeseTeachMenu menu, CheeseFormRecipe categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return categoryRecipe;
	}

	@Override
	protected void serializePayload(CheeseTeachMenu menu, CheeseFormRecipe recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		NBTUtils2.serializeCollection(tag, "input", input, ItemStack::serializeNBT);
	}

}
