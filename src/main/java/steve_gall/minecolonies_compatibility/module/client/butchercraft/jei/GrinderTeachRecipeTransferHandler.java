package steve_gall.minecolonies_compatibility.module.client.butchercraft.jei;

import java.util.ArrayList;
import java.util.Optional;

import com.lance5057.butchercraft.integration.jei.categories.GrinderRecipeCategory;
import com.lance5057.butchercraft.workstations.grinder.GrinderRecipe;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.client.jei.TeachRecipeTransferHandler;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.menu.GrinderTeachMenu;

public class GrinderTeachRecipeTransferHandler extends TeachRecipeTransferHandler<GrinderTeachMenu, GrinderRecipe, GrinderRecipe>
{
	public GrinderTeachRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper)
	{
		super(recipeTransferHandlerHelper);
	}

	@Override
	public Class<? extends GrinderTeachMenu> getContainerClass()
	{
		return GrinderTeachMenu.class;
	}

	@Override
	public Optional<MenuType<GrinderTeachMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<GrinderRecipe> getRecipeType()
	{
		return GrinderRecipeCategory.TYPE;
	}

	@Override
	protected GrinderRecipe getRecipe(GrinderTeachMenu menu, GrinderRecipe categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return categoryRecipe;
	}

	@Override
	protected void serializePayload(GrinderTeachMenu menu, GrinderRecipe recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		var catalyst = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.CATALYST);
		var list = new ArrayList<ItemStack>();
		list.add(input.get(0));
		list.add(catalyst.get(0));
		list.add(catalyst.size() > 1 ? catalyst.get(1) : ItemStack.EMPTY);
		NBTUtils2.serializeCollection(tag, "input", list, ItemStack::serializeNBT);
	}

}
