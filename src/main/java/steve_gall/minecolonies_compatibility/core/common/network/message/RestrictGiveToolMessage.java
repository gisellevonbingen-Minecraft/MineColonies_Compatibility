package steve_gall.minecolonies_compatibility.core.common.network.message;

import com.minecolonies.api.util.InventoryUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModule;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModuleView;
import steve_gall.minecolonies_compatibility.core.common.init.ModItems;

public class RestrictGiveToolMessage extends BuildingModuleMessage
{
	private final Component moduleDesc;

	public RestrictGiveToolMessage(IRestrictableModuleView module, Component moduleDesc)
	{
		super(module);
		this.moduleDesc = moduleDesc;
	}

	public RestrictGiveToolMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.moduleDesc = buffer.readComponent();
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeComponent(this.moduleDesc);
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		super.handle(context);

		if (this.getModule() instanceof IRestrictableModule module)
		{
			var item = ModItems.RESTRICT_TOOL.get();
			var player = context.getSender();
			var tool = InventoryUtils.getOrCreateItemAndPutToHotbarAndSelectOrDrop(item, player, item::getDefaultInstance, true);
			item.setModule(tool, module, this.moduleDesc);

			player.getInventory().setChanged();
		}

	}

	public Component getModuleDesc()
	{
		return this.moduleDesc;
	}

}
