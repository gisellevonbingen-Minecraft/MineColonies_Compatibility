package steve_gall.minecolonies_compatibility.core.common.building.module;

import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.core.common.network.message.SmithingOpenTeachMessage;

public class SmithingCraftingModuleView extends CraftingModuleView
{
	public SmithingCraftingModuleView()
	{

	}

	@Override
	public void openCraftingGUI()
	{
		PacketDistributor.sendToServer(new SmithingOpenTeachMessage(this));
	}

}
