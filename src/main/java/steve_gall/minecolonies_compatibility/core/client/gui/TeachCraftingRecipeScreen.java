package steve_gall.minecolonies_compatibility.core.client.gui;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.util.constant.TranslationConstants;
import com.minecolonies.core.Network;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.minecolonies.core.network.messages.server.colony.building.worker.AddRemoveRecipeMessage;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public abstract class TeachCraftingRecipeScreen<MENU extends TeachRecipeMenu<RECIPE>, RECIPE> extends TeachRecipeScreen<MENU, RECIPE>
{
	private static final Component TEXT_WARNING_MAXIMUM_NUMBER_RECIPES = Component.translatable(TranslationConstants.WARNING_MAXIMUM_NUMBER_RECIPES);

	protected final CraftingModuleView module;

	public TeachCraftingRecipeScreen(MENU menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);

		this.module = (CraftingModuleView) menu.getModulePos().getModuleView();
	}

	@Override
	protected Component getError()
	{
		if (!this.module.canLearn(this.getCraftingType()))
		{
			return TEXT_WARNING_MAXIMUM_NUMBER_RECIPES;
		}

		return super.getError();
	}

	@Override
	protected void onDone(RECIPE recipe, List<ItemStorage> input)
	{
		var storage = this.createRecipeStorage(recipe, input).wrap();
		Network.getNetwork().sendToServer(new AddRemoveRecipeMessage(this.module.getBuildingView(), false, storage, this.module.getProducer().getRuntimeID()));
	}

	public abstract CraftingType getCraftingType();

	protected abstract ICustomizedRecipeStorage createRecipeStorage(RECIPE recipe, List<ItemStorage> input);

	public CraftingModuleView getModule()
	{
		return this.module;
	}

}
