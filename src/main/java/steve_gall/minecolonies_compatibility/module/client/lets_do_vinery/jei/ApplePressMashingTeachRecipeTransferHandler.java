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
import net.satisfy.vinery.core.compat.jei.category.ApplePressMashingCategory;
import net.satisfy.vinery.core.recipe.ApplePressMashingRecipe;
import net.satisfy.vinery.core.recipe.input.ApplePressMashingRecipeInput;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.client.jei.TeachRecipeTransferHandler;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.menu.ApplePressMashingTeachMenu;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class ApplePressMashingTeachRecipeTransferHandler extends TeachRecipeTransferHandler<ApplePressMashingTeachMenu, RecipeHolder<ApplePressMashingRecipe>, ApplePressMashingRecipeInput, ApplePressMashingRecipe>
{
	private static final ResourceLocation HOLDER_NAME = MineColoniesCompatibility.rl("dummy");

	public ApplePressMashingTeachRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper)
	{
		super(recipeTransferHandlerHelper);
	}

	@Override
	public Class<? extends ApplePressMashingTeachMenu> getContainerClass()
	{
		return ApplePressMashingTeachMenu.class;
	}

	@Override
	public Optional<MenuType<ApplePressMashingTeachMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<ApplePressMashingRecipe> getRecipeType()
	{
		return ApplePressMashingCategory.APPLE_PRESS_MASHING_TYPE;
	}

	@Override
	protected RecipeHolder<ApplePressMashingRecipe> getRecipe(ApplePressMashingTeachMenu menu, ApplePressMashingRecipe categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return new RecipeHolder<>(HOLDER_NAME, categoryRecipe);
	}

	@Override
	protected void serializePayload(ApplePressMashingTeachMenu menu, RecipeHolder<ApplePressMashingRecipe> recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		tag.put("input", ItemSerializationHelper.serializeTag(player.registryAccess(), input.get(0)));
	}

}
