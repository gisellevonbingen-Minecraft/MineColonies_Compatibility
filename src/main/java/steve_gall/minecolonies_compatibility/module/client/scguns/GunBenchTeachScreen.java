package steve_gall.minecolonies_compatibility.module.client.scguns;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeHolder;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachCraftingRecipeScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.scguns.crafting.GunBenchRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.scguns.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.scguns.menu.GunBenchTeachMenu;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;
import top.ribs.scguns.client.screen.GunBenchRecipe;

public class GunBenchTeachScreen extends TeachCraftingRecipeScreen<GunBenchTeachMenu, RecipeHolder<GunBenchRecipe>>
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/scguns_gun_bench_teach.png");

	public GunBenchTeachScreen(GunBenchTeachMenu menu, Inventory inventory, Component title)
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
		return ModuleCraftingTypes.GUN_BENCH.get();
	}

	@Override
	protected ICustomizedRecipeStorage createRecipeStorage(RecipeHolder<GunBenchRecipe> recipe, List<ItemStorage> input)
	{
		var resultContainer = this.menu.getResultContainer();
		var output = resultContainer.getItem(0);
		var ingredients = input.subList(0, GunBenchTeachMenu.INGREDIENT_SLOTS);
		var blueprint = input.get(GunBenchTeachMenu.BLUEPRINT_SLOT);
		return new GunBenchRecipeStorage(recipe.id(), ingredients, blueprint, output);
	}

}
