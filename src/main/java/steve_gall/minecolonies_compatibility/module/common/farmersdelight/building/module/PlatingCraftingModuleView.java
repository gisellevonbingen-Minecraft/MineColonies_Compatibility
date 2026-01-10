package steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module;

import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.network.PlatingOpenTeachMessage;

public class PlatingCraftingModuleView extends CraftingModuleView
{
	public PlatingCraftingModuleView()
	{

	}

	@Override
	public void openCraftingGUI()
	{
		MineColoniesCompatibility.network().sendToServer(new PlatingOpenTeachMessage(this));
	}

}
