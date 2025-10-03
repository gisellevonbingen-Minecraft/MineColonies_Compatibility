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
import net.satisfy.meadow.core.compat.jei.category.WoodCutterCategory;
import net.satisfy.meadow.core.recipes.WoodcuttingRecipe;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.client.jei.TeachRecipeTransferHandler;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.menu.WoodcuttingTeachMenu;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class WoodcuttingTeachRecipeTransferHandler extends TeachRecipeTransferHandler<WoodcuttingTeachMenu, RecipeHolder<WoodcuttingRecipe>, RecipeInput, WoodcuttingRecipe>
{
	private static final ResourceLocation HOLDER_NAME = MineColoniesCompatibility.rl("dummy");

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
	protected RecipeHolder<WoodcuttingRecipe> getRecipe(WoodcuttingTeachMenu menu, WoodcuttingRecipe categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return new RecipeHolder<>(HOLDER_NAME, categoryRecipe);
	}

	@Override
	protected void serializePayload(WoodcuttingTeachMenu menu, RecipeHolder<WoodcuttingRecipe> recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		tag.put("input", ItemSerializationHelper.serializeTag(player.registryAccess(), input.get(0)));
	}

}
