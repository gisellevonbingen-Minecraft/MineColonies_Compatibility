package steve_gall.minecolonies_compatibility.core.common.network.message;

import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.Utils;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModule;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModuleView;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.init.ModItems;

public class RestrictGiveToolMessage extends BuildingModuleMessage
{
	public static final CustomPacketPayload.Type<RestrictGiveToolMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("restrict_give_tool"));

	private final Component moduleDesc;

	public RestrictGiveToolMessage(IRestrictableModuleView module, Component moduleDesc)
	{
		super(module);
		this.moduleDesc = moduleDesc;
	}

	public RestrictGiveToolMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.moduleDesc = Utils.deserializeCodecMess(ComponentSerialization.STREAM_CODEC, buffer);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		Utils.serializeCodecMess(ComponentSerialization.STREAM_CODEC, buffer, this.moduleDesc);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		if (this.getModule() instanceof IRestrictableModule module)
		{
			var item = ModItems.RESTRICT_TOOL.get();
			var player = context.player();
			var tool = InventoryUtils.getOrCreateItemAndPutToHotbarAndSelectOrDrop(item, player, item::getDefaultInstance, true);
			item.setModule(tool, module, this.moduleDesc);

			player.getInventory().setChanged();
		}

	}

	@Override
	public CustomPacketPayload.Type<RestrictGiveToolMessage> type()
	{
		return TYPE;
	}

	public Component getModuleDesc()
	{
		return this.moduleDesc;
	}

}
