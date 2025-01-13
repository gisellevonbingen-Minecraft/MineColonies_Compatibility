package steve_gall.minecolonies_compatibility.module.common.butchercraft.building.module;

import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.network.GrinderOpenTeachMessage;

public class GrinderCraftingModuleView extends CraftingModuleView
{
	public GrinderCraftingModuleView()
	{

	}

	@Override
	public void openCraftingGUI()
	{
		MineColoniesCompatibility.network().sendToServer(new GrinderOpenTeachMessage(this));
	}

}
