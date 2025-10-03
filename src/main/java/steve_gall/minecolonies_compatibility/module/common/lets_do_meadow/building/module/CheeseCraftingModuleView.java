package steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.building.module;

import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.network.CheeseOpenTeachMessage;

public class CheeseCraftingModuleView extends CraftingModuleView
{
	public CheeseCraftingModuleView()
	{

	}

	@Override
	public void openCraftingGUI()
	{
		PacketDistributor.sendToServer(new CheeseOpenTeachMessage(this));
	}

}
