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
	private final String moduleName;

	public RestrictGiveToolMessage(IRestrictableModuleView module, String moduleName)
	{
		super(module);
		this.moduleName = moduleName;
	}

	public RestrictGiveToolMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.moduleName = buffer.readUtf();
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeUtf(this.moduleName);
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
			item.setModule(tool, module, Component.literal(this.moduleName));

			player.getInventory().setChanged();
		}

	}

	public String getModuleName()
	{
		return this.moduleName;
	}

}
