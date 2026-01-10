package steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module;

import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.network.PlatingOpenTeachMessage;

public class PlatingCraftingModuleView extends CraftingModuleView
{
	public PlatingCraftingModuleView()
	{

	}

	@Override
	public void openCraftingGUI()
	{
		PacketDistributor.sendToServer(new PlatingOpenTeachMessage(this));
	}

}
