package steve_gall.minecolonies_compatibility.module.client.lets_do_meadow.jei;

import java.util.Optional;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.satisfy.meadow.core.compat.jei.category.CookingCauldronCategory;
import net.satisfy.meadow.core.recipes.CookingCauldronRecipe;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.client.jei.TeachRecipeTransferHandler;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.menu.CookingTeachMenu;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class CookingTeachRecipeTransferHandler extends TeachRecipeTransferHandler<CookingTeachMenu, RecipeHolder<CookingCauldronRecipe>, RecipeInput, CookingCauldronRecipe>
{
	private static final ResourceLocation HOLDER_NAME = MineColoniesCompatibility.rl("dummy");

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
	public RecipeType<CookingCauldronRecipe> getRecipeType()
	{
		return CookingCauldronCategory.COOKING_CAULDRON;
	}

	@Override
	protected RecipeHolder<CookingCauldronRecipe> getRecipe(CookingTeachMenu menu, CookingCauldronRecipe categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return new RecipeHolder<>(HOLDER_NAME, categoryRecipe);
	}

	@Override
	protected void serializePayload(CookingTeachMenu menu, RecipeHolder<CookingCauldronRecipe> recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		NBTUtils2.serializeCollection(tag, "input", input, ItemSerializationHelper.serializerTag(player.registryAccess()));
	}

}
