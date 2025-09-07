package steve_gall.minecolonies_compatibility.module.client.farmersdelight.jei;

import java.util.Optional;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.client.jei.TeachRecipeTransferHandler;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.CookingTeachMenu;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;

public class CookingTeachRecipeTransferHandler extends TeachRecipeTransferHandler<CookingTeachMenu, RecipeHolder<CookingPotRecipe>, RecipeWrapper, RecipeHolder<CookingPotRecipe>>
{
	public CookingTeachRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper)
	{
		super(recipeTransferHandlerHelper);
	}

	@Override
	public Class<? extends CookingTeachMenu> getContainerClass()
	{
		return CookingTeachMenu.class;
	}

	@Override
	public Optional<MenuType<CookingTeachMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<RecipeHolder<CookingPotRecipe>> getRecipeType()
	{
		return FDRecipeTypes.COOKING;
	}

	@Override
	protected RecipeHolder<CookingPotRecipe> getRecipe(CookingTeachMenu menu, RecipeHolder<CookingPotRecipe> categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return categoryRecipe;
	}

	@Override
	protected void serializePayload(CookingTeachMenu menu, RecipeHolder<CookingPotRecipe> recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		NBTUtils2.serializeCollection(tag, "input", input, ItemSerializationHelper.serializerTag(player.registryAccess()));
	}

}
