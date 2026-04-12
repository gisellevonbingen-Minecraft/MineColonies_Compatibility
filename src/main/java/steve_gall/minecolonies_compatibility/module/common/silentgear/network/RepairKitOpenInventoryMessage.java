package steve_gall.minecolonies_compatibility.module.common.silentgear.network;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.ModuleMenuOpenMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.building.module.RepairMaterialListModule;
import steve_gall.minecolonies_compatibility.module.common.silentgear.menu.RepairKitInventoryMenu;

public class RepairKitOpenInventoryMessage extends ModuleMenuOpenMessage
{
	public static final CustomPacketPayload.Type<RepairKitOpenInventoryMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("silentgear_repair_kit_open_inventory"));

	public RepairKitOpenInventoryMessage(RepairMaterialListModule.View module)
	{
		super(module);
	}

	public RepairKitOpenInventoryMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);
	}

	@Override
	public CustomPacketPayload.Type<RepairKitOpenInventoryMessage> type()
	{
		return TYPE;
	}

	@Override
	protected AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player, IBuildingModule module)
	{
		return new RepairKitInventoryMenu(windowId, inventory, (RepairMaterialListModule) module);
	}

	@Override
	protected Component getDisplayName()
	{
		return Component.translatable("minecolonies_compatibility.text.silentgear_repair_kit_inventory");
	}

	@Override
	protected void toBuffer(RegistryFriendlyByteBuf buffer, IBuildingModule module)
	{
		super.toBuffer(buffer, module);
	}

}
