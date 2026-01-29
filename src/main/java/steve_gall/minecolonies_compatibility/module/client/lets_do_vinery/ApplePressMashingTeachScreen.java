package steve_gall.minecolonies_compatibility.module.client.lets_do_vinery;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.satisfy.vinery.core.recipe.ApplePressMashingRecipe;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachCraftingRecipeScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting.ApplePressMashingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.menu.ApplePressMashingTeachMenu;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public class ApplePressMashingTeachScreen extends TeachCraftingRecipeScreen<ApplePressMashingTeachMenu, RecipeHolder<ApplePressMashingRecipe>>
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/lets_do_vinery_apple_press_mashing_teach.png");

	public ApplePressMashingTeachScreen(ApplePressMashingTeachMenu menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);

		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	public ResourceLocation getTexture()
	{
		return TEXTURE;
	}

	@Override
	public CraftingType getCraftingType()
	{
		return ModuleCraftingTypes.APPLE_PRESS_MASHING.get();
	}

	@Override
	protected ICustomizedRecipeStorage createRecipeStorage(RecipeHolder<ApplePressMashingRecipe> recipe, List<ItemStorage> input)
	{
		var resultContainer = this.menu.getResultContainer();
		var output = resultContainer.getItem(0);
		return new ApplePressMashingRecipeStorage(recipe.id(), input, output);
	}

}
