// package steve_gall.minecolonies_compatibility.module.client.lets_do_vinery.jei;
//
// import java.util.Optional;
//
// import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
// import mezz.jei.api.recipe.RecipeIngredientRole;
// import mezz.jei.api.recipe.RecipeType;
// import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
// import net.minecraft.nbt.CompoundTag;
// import net.minecraft.world.entity.player.Player;
// import net.minecraft.world.inventory.MenuType;
// import net.satisfy.vinery.core.compat.rei.press.ApplePressCategory;
// import net.satisfy.vinery.core.recipe.ApplePressMashingRecipe;
// import net.satisfy.vinery.recipe.ApplePressRecipe;
// import steve_gall.minecolonies_compatibility.module.client.jei.TeachRecipeTransferHandler;
// import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.menu.ApplePressMashingTeachMenu;
//
// public class ApplePressMashingTeachRecipeTransferHandler extends TeachRecipeTransferHandler<ApplePressMashingTeachMenu, ApplePressMashingRecipe, ApplePressMashingRecipe>
// {
// public ApplePressMashingTeachRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper)
// {
// super(recipeTransferHandlerHelper);
// }
//
// @Override
// public Class<? extends ApplePressMashingTeachMenu> getContainerClass()
// {
// return ApplePressMashingTeachMenu.class;
// }
//
// @Override
// public Optional<MenuType<ApplePressMashingTeachMenu>> getMenuType()
// {
// return Optional.empty();
// }
//
// @Override
// public RecipeType<ApplePressMashingRecipe> getRecipeType()
// {
// return ApplePressCategoryc.APPLE_PRESS;
// }
//
// @Override
// protected ApplePressMashingRecipe getRecipe(ApplePressMashingTeachMenu menu, ApplePressMashingRecipe categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
// {
// return categoryRecipe;
// }
//
// @Override
// protected void serializePayload(ApplePressMashingTeachMenu menu, ApplePressMashingRecipe recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
// {
// var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
// tag.put("input", input.get(0).serializeNBT());
// }
//
// }
