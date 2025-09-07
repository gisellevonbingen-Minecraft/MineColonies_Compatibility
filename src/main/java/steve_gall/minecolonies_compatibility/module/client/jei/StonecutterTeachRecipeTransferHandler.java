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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import steve_gall.minecolonies_compatibility.core.common.inventory.StonecutterTeachMenu;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class StonecutterTeachRecipeTransferHandler extends TeachRecipeTransferHandler<StonecutterTeachMenu, RecipeHolder<StonecutterRecipe>, SingleRecipeInput, RecipeHolder<StonecutterRecipe>>
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
	public RecipeType<RecipeHolder<StonecutterRecipe>> getRecipeType()
	{
		return RecipeTypes.STONECUTTING;
	}

	@Override
	protected RecipeHolder<StonecutterRecipe> getRecipe(StonecutterTeachMenu menu, RecipeHolder<StonecutterRecipe> categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return categoryRecipe;
	}

	@Override
	protected void serializePayload(StonecutterTeachMenu menu, RecipeHolder<StonecutterRecipe> recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		NBTUtils2.serializeCollection(tag, "input", input, ItemSerializationHelper.serializerTag(player.registryAccess()));
	}

}
