package steve_gall.minecolonies_compatibility.module.client.jei;

import java.util.Optional;

import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import steve_gall.minecolonies_compatibility.core.common.inventory.SmithingTeachMenu;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class SmithingTeachRecipeTransferHandler extends TeachRecipeTransferHandler<SmithingTeachMenu, RecipeHolder<SmithingRecipe>, SmithingRecipeInput, RecipeHolder<SmithingRecipe>>
{
	public SmithingTeachRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper)
	{
		super(recipeTransferHandlerHelper);
	}

	@Override
	public Class<? extends SmithingTeachMenu> getContainerClass()
	{
		return SmithingTeachMenu.class;
	}

	@Override
	public Optional<MenuType<SmithingTeachMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<RecipeHolder<SmithingRecipe>> getRecipeType()
	{
		return RecipeTypes.SMITHING;
	}

	@Override
	protected RecipeHolder<SmithingRecipe> getRecipe(SmithingTeachMenu menu, RecipeHolder<SmithingRecipe> categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return categoryRecipe;
	}

	@Override
	protected Component getError(SmithingTeachMenu menu, RecipeHolder<SmithingRecipe> recipe, IRecipeSlotsView recipeSlots, Player player)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		var template = input.get(0);
		var base = input.get(1);
		var addition = input.get(2);
		var error = menu.getRecipeError(new SmithingRecipeInput(template, base, addition));

		if (error != null)
		{
			return error;
		}

		return super.getError(menu, recipe, recipeSlots, player);
	}

	@Override
	protected void serializePayload(SmithingTeachMenu menu, RecipeHolder<SmithingRecipe> recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		NBTUtils2.serializeCollection(tag, "input", input, ItemSerializationHelper.serializerTag(player.registryAccess()));
	}

}
