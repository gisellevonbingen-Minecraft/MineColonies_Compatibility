package steve_gall.minecolonies_compatibility.module.client.lets_do_vinery.jei;

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
import net.satisfy.vinery.core.compat.jei.category.ApplePressFermentingCategory;
import net.satisfy.vinery.core.recipe.ApplePressFermentingRecipe;
import net.satisfy.vinery.core.recipe.input.ApplePressFermentingRecipeInput;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.client.jei.TeachRecipeTransferHandler;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.menu.ApplePressFermentingTeachMenu;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class ApplePressFermentingTeachRecipeTransferHandler extends TeachRecipeTransferHandler<ApplePressFermentingTeachMenu, RecipeHolder<ApplePressFermentingRecipe>, ApplePressFermentingRecipeInput, ApplePressFermentingRecipe>
{
	private static final ResourceLocation HOLDER_NAME = MineColoniesCompatibility.rl("dummy");

	public ApplePressFermentingTeachRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper)
	{
		super(recipeTransferHandlerHelper);
	}

	@Override
	public Class<? extends ApplePressFermentingTeachMenu> getContainerClass()
	{
		return ApplePressFermentingTeachMenu.class;
	}

	@Override
	public Optional<MenuType<ApplePressFermentingTeachMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<ApplePressFermentingRecipe> getRecipeType()
	{
		return ApplePressFermentingCategory.APPLE_PRESS_TYPE;
	}

	@Override
	protected RecipeHolder<ApplePressFermentingRecipe> getRecipe(ApplePressFermentingTeachMenu menu, ApplePressFermentingRecipe categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return new RecipeHolder<>(HOLDER_NAME, categoryRecipe);
	}

	@Override
	protected void serializePayload(ApplePressFermentingTeachMenu menu, RecipeHolder<ApplePressFermentingRecipe> recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		tag.put("input", ItemSerializationHelper.serializeTag(player.registryAccess(), input.get(0)));
	}

}
