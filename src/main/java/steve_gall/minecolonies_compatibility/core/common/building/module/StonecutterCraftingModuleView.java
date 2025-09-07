package steve_gall.minecolonies_compatibility.core.common.building.module;

import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.core.common.network.message.StonecutterOpenTeachMessage;

public class StonecutterCraftingModuleView extends CraftingModuleView
{
	public StonecutterCraftingModuleView()
	{

	}

	@Override
	public void openCraftingGUI()
	{
		PacketDistributor.sendToServer(new StonecutterOpenTeachMessage(this));
	}

}
