package steve_gall.minecolonies_compatibility.module.client.cgm;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.mrcrayfish.guns.crafting.WorkbenchRecipe;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachCraftingRecipeScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.cgm.crafting.WorkbenchRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.cgm.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.cgm.menu.WorkbenchTeachMenu;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public class WorkbenchTeachScreen extends TeachCraftingRecipeScreen<WorkbenchTeachMenu, WorkbenchRecipe>
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/cgm_workbench_teach.png");

	public WorkbenchTeachScreen(WorkbenchTeachMenu menu, Inventory inventory, Component title)
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
		return ModuleCraftingTypes.WORKBENCH.get();
	}

	@Override
	protected ICustomizedRecipeStorage createRecipeStorage(WorkbenchRecipe recipe, List<ItemStorage> input)
	{
		var resultContainer = this.menu.getResultContainer();
		var output = resultContainer.getItem(0);
		return new WorkbenchRecipeStorage(recipe.getId(), input, output);
	}

}
