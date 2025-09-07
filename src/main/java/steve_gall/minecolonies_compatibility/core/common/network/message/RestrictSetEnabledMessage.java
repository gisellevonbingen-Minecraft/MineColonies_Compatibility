package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModule;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModuleView;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class RestrictSetEnabledMessage extends BuildingModuleMessage
{
	public static final CustomPacketPayload.Type<RestrictSetEnabledMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("restrict_set_enabled"));

	private final boolean enabled;

	public RestrictSetEnabledMessage(IRestrictableModuleView module, boolean enabled)
	{
		super(module);

		this.enabled = enabled;
	}

	public RestrictSetEnabledMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.enabled = buffer.readBoolean();
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeBoolean(this.enabled);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		if (this.getModule() instanceof IRestrictableModule module)
		{
			module.setRestrictEnabled(this.enabled);
		}

	}

	@Override
	public CustomPacketPayload.Type<RestrictSetEnabledMessage> type()
	{
		return TYPE;
	}

	public boolean isRestrictEnabled()
	{
		return this.enabled;
	}

}
