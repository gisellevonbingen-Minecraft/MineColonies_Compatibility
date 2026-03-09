package steve_gall.minecolonies_compatibility.module.client.cgm.jei;

import java.util.ArrayList;
import java.util.Optional;

import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.item.IColored;

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
import steve_gall.minecolonies_compatibility.module.common.cgm.menu.WorkbenchTeachMenu;

public class WorkbenchRecipeTransferHandler extends TeachRecipeTransferHandler<WorkbenchTeachMenu, WorkbenchRecipe, WorkbenchRecipe>
{
	private final RecipeType<WorkbenchRecipe> recipeType;

	public WorkbenchRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper, RecipeType<WorkbenchRecipe> recipeType)
	{
		super(recipeTransferHandlerHelper);

		this.recipeType = recipeType;
	}

	@Override
	public Class<? extends WorkbenchTeachMenu> getContainerClass()
	{
		return WorkbenchTeachMenu.class;
	}

	@Override
	public Optional<MenuType<WorkbenchTeachMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<WorkbenchRecipe> getRecipeType()
	{
		return this.recipeType;
	}

	@Override
	protected WorkbenchRecipe getRecipe(WorkbenchTeachMenu menu, WorkbenchRecipe categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return categoryRecipe;
	}

	@Override
	protected void serializePayload(WorkbenchTeachMenu menu, WorkbenchRecipe recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = new ArrayList<>(this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT));

		if (IColored.isDyeable(recipe.getResultItem(player.level().registryAccess())))
		{
			input.remove(0);
		}

		NBTUtils2.serializeCollection(tag, "input", input, ItemStack::serializeNBT);
	}

}
