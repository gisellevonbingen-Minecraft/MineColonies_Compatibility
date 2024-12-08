package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.building.modules;

import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.network.ApplePressFermentingOpenTeachMessage;

public class ApplePressFermentingCraftingModuleView extends CraftingModuleView
{
	public ApplePressFermentingCraftingModuleView()
	{

	}

	@Override
	public void openCraftingGUI()
	{
		MineColoniesCompatibility.network().sendToServer(new ApplePressFermentingOpenTeachMessage(this));
	}

}
