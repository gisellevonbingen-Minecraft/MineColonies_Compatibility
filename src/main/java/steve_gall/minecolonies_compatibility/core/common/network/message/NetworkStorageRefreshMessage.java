package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModuleView;

public class NetworkStorageRefreshMessage extends BuildingModuleMessage
{
	public NetworkStorageRefreshMessage(NetworkStorageModuleView module)
	{
		super(module);
	}

	public NetworkStorageRefreshMessage(FriendlyByteBuf buffer)
	{
		super(buffer);
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);
	}

	@Override
	public void handle(Context context)
	{
		super.handle(context);

		var player = context.getSender();

		if (player == null)
		{
			return;
		}

		if (this.getModule() instanceof NetworkStorageModule module)
		{
			module.requestFindWorkingBlocks();
		}

	}

}
