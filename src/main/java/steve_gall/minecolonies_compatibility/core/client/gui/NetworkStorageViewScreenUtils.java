package steve_gall.minecolonies_compatibility.core.client.gui;

import net.minecraft.network.chat.Component;
import steve_gall.minecolonies_compatibility.api.common.building.module.INetworkStorageView;

public class NetworkStorageViewScreenUtils
{
	public static final Component TEXT_NOT_LINKED = Component.translatable("minecolonies_compatibility.gui.not_linked");
	public static final String TEXT_LINKED = "minecolonies_compatibility.gui.linked_pos";

	public static Component getModuleText(INetworkStorageView view)
	{
		var module = view.getLinkedModuleView();

		if (module != null)
		{
			var pos = module.getBuildingView().getID();
			return Component.translatable(TEXT_LINKED, pos.getX(), pos.getY(), pos.getZ());
		}
		else
		{
			return TEXT_NOT_LINKED;
		}

	}

	private NetworkStorageViewScreenUtils()
	{

	}

}
