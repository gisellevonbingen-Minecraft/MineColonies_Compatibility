package steve_gall.minecolonies_compatibility.core.common.building.module;

import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.StonecutterOpenTeachMessage;

public class StonecutterCraftingModuleView extends CraftingModuleView
{
	public StonecutterCraftingModuleView()
	{

	}

	@Override
	public void openCraftingGUI()
	{
		MineColoniesCompatibility.network().sendToServer(new StonecutterOpenTeachMessage(this));
	}

}
