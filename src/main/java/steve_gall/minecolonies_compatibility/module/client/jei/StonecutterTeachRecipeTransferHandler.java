package steve_gall.minecolonies_compatibility.module.client.jei;

import java.util.Optional;

import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import steve_gall.minecolonies_compatibility.core.common.inventory.StonecutterTeachMenu;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;

public class StonecutterTeachRecipeTransferHandler extends TeachRecipeTransferHandler<StonecutterTeachMenu, StonecutterRecipe, StonecutterRecipe>
{
	public StonecutterTeachRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper)
	{
		super(recipeTransferHandlerHelper);
	}

	@Override
	public Class<? extends StonecutterTeachMenu> getContainerClass()
	{
		return StonecutterTeachMenu.class;
	}

	@Override
	public Optional<MenuType<StonecutterTeachMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<StonecutterRecipe> getRecipeType()
	{
		return RecipeTypes.STONECUTTING;
	}

	@Override
	protected StonecutterRecipe getRecipe(StonecutterTeachMenu menu, StonecutterRecipe categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return categoryRecipe;
	}

	@Override
	protected void serializePayload(StonecutterTeachMenu menu, StonecutterRecipe recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		NBTUtils2.serializeCollection(tag, "input", input, ItemStack::serializeNBT);
	}

}
