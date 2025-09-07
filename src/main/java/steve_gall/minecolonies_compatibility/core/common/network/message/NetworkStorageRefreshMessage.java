package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModuleView;

public class NetworkStorageRefreshMessage extends BuildingModuleMessage
{
	public static final CustomPacketPayload.Type<NetworkStorageRefreshMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("network_storage_refresh"));

	public NetworkStorageRefreshMessage(NetworkStorageModuleView module)
	{
		super(module);
	}

	public NetworkStorageRefreshMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		if (this.getModule() instanceof NetworkStorageModule module)
		{
			module.requestFindWorkingBlocks();
		}

	}

	@Override
	public CustomPacketPayload.Type<NetworkStorageRefreshMessage> type()
	{
		return TYPE;
	}

}
