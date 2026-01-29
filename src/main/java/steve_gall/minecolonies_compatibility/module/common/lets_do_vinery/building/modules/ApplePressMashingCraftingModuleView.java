package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.building.modules;

import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.network.ApplePressMashingOpenTeachMessage;

public class ApplePressMashingCraftingModuleView extends CraftingModuleView
{
	public ApplePressMashingCraftingModuleView()
	{

	}

	@Override
	public void openCraftingGUI()
	{
		PacketDistributor.sendToServer(new ApplePressMashingOpenTeachMessage(this));
	}

}
