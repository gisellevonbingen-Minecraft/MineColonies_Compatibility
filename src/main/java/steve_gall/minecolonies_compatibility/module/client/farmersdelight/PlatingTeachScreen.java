package steve_gall.minecolonies_compatibility.module.client.farmersdelight;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachCraftingRecipeScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.PlatingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.PlatingTeachMenu;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public class PlatingTeachScreen extends TeachCraftingRecipeScreen<PlatingTeachMenu, PlatingRecipeStorage>
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/farmers_plating_teach.png");

	public PlatingTeachScreen(PlatingTeachMenu menu, Inventory inventory, Component title)
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
		return ModuleCraftingTypes.PLATING.get();
	}

	@Override
	protected ICustomizedRecipeStorage createRecipeStorage(PlatingRecipeStorage recipe, List<ItemStorage> input)
	{
		return recipe;
	}

}
