package steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module;

import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.FarmersTeachCookingOpenMessage;

public class CookingCraftingModuleView extends CraftingModuleView
{
	public CookingCraftingModuleView()
	{

	}

	@Override
	public void openCraftingGUI()
	{
		MineColoniesCompatibility.network().sendToServer(new FarmersTeachCookingOpenMessage(this));
	}

}
