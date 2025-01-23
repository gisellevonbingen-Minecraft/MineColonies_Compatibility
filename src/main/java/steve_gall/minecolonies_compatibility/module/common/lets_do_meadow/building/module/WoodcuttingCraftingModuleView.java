package steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.building.module;

import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.network.WoodcuttingOpenTeachMessage;

public class WoodcuttingCraftingModuleView extends CraftingModuleView
{
	public WoodcuttingCraftingModuleView()
	{

	}

	@Override
	public void openCraftingGUI()
	{
		MineColoniesCompatibility.network().sendToServer(new WoodcuttingOpenTeachMessage(this));
	}

}
