package steve_gall.minecolonies_compatibility.module.client.scguns.jei;

import java.util.ArrayList;
import java.util.Optional;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.client.jei.TeachRecipeTransferHandler;
import steve_gall.minecolonies_compatibility.module.common.scguns.menu.GunBenchTeachMenu;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;
import top.ribs.scguns.client.screen.GunBenchRecipe;

public class GunBenchRecipeTransferHandler extends TeachRecipeTransferHandler<GunBenchTeachMenu, RecipeHolder<GunBenchRecipe>, GunBenchRecipe.Input, RecipeHolder<GunBenchRecipe>>
{
	private final RecipeType<RecipeHolder<GunBenchRecipe>> recipeType;

	public GunBenchRecipeTransferHandler(IRecipeTransferHandlerHelper recipeTransferHandlerHelper, RecipeType<RecipeHolder<GunBenchRecipe>> recipeType)
	{
		super(recipeTransferHandlerHelper);

		this.recipeType = recipeType;
	}

	@Override
	public Class<? extends GunBenchTeachMenu> getContainerClass()
	{
		return GunBenchTeachMenu.class;
	}

	@Override
	public Optional<MenuType<GunBenchTeachMenu>> getMenuType()
	{
		return Optional.empty();
	}

	@Override
	public RecipeType<RecipeHolder<GunBenchRecipe>> getRecipeType()
	{
		return this.recipeType;
	}

	@Override
	protected RecipeHolder<GunBenchRecipe> getRecipe(GunBenchTeachMenu menu, RecipeHolder<GunBenchRecipe> categoryRecipe, IRecipeSlotsView recipeSlots, Player player)
	{
		return categoryRecipe;
	}

	@Override
	protected void serializePayload(GunBenchTeachMenu menu, RecipeHolder<GunBenchRecipe> holder, IRecipeSlotsView recipeSlots, Player player, CompoundTag tag)
	{
		var input = this.getDisplayedItemStacks(recipeSlots, RecipeIngredientRole.INPUT);
		var recipe = holder.value();
		var blueprint = ItemStack.EMPTY;

		if (recipe.getBlueprint().isEmpty())
		{
			blueprint = ItemStack.EMPTY;
		}
		else
		{
			blueprint = input.get(input.size() - 1);
		}

		var originalIngredients = recipe.getIngredients();
		var ingredients = new ArrayList<ItemStack>();
		var inputIndex = 0;

		for (var originalIngredient : originalIngredients)
		{
			if (originalIngredient.isEmpty())
			{
				ingredients.add(ItemStack.EMPTY);
			}
			else
			{
				ingredients.add(input.get(inputIndex++));
			}

		}

		var registryAccess = player.registryAccess();
		NBTUtils2.serializeCollection(tag, "ingredients", ingredients, ItemSerializationHelper.serializerTag(registryAccess));
		tag.put("blueprint", ItemSerializationHelper.serializeTag(registryAccess, blueprint));
	}

}
