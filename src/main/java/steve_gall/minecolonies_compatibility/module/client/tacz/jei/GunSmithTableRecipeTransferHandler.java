package steve_gall.minecolonies_compatibility.module.client.tacz.jei;

import java.util.Optional;

import com.tacz.guns.crafting.GunSmithTableRecipe;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.client.jei.TeachRecipeTransferHandler;
import steve_gall.minecolonies_compatibility.module.common.tacz.menu.GunSmithTableTeachMenu;
import steve_gall.minecolonies_tweaks.core.common.util.SerializationHelper;

public class GunSmithTableRecipeTransferHandler extends TeachRecipeTransferHandler<GunSmithTableTeachMenu, RecipeHolder<GunSmithTableRecipe>, SmithingRecipeInput, RecipeHolder<GunSmithTableRecipe>>
{
	private final RecipeType<RecipeHolder<GunSmithTableRecipe>> recipeType;

	public GunSmithTableRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper, RecipeType<RecipeHolder<GunSmithTableRecipe>> recipeType)
	{
		super(recipeTransferHandlerHelper);

		this.recipeType = recipeType;
	}

	@Override
	public Class<? extends GunSmithTableTeachMenu> getContainerClass()
	{
		return GunSmithTableTeachMenu.class;
	}

	@Override
	public Optional<MenuType<GunSmithTableTeachMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<RecipeHolder<GunSmithTableRecipe>> getRecipeType()
	{
		return this.recipeType;
	}

	@Override
	protected RecipeHolder<GunSmithTableRecipe> getRecipe(GunSmithTableTeachMenu menu, RecipeHolder<GunSmithTableRecipe> categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return categoryRecipe;
	}

	@Override
	protected void serializePayload(GunSmithTableTeachMenu menu, RecipeHolder<GunSmithTableRecipe> recipe, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStorages(recipeSlots, RecipeIngredientRole.INPUT);
		NBTUtils2.serializeCollection(tag, "input", input, SerializationHelper.serializerTag(player.registryAccess()));
	}

}
