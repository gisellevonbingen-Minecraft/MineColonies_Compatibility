package steve_gall.minecolonies_compatibility.module.common.silentgear.network;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import steve_gall.minecolonies_compatibility.core.common.network.message.ModuleMenuOpenMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.building.module.RepairMaterialListModule;
import steve_gall.minecolonies_compatibility.module.common.silentgear.menu.RepairKitInventoryMenu;

public class RepairKitOpenInventoryMessage extends ModuleMenuOpenMessage
{
	public RepairKitOpenInventoryMessage(RepairMaterialListModule.View module)
	{
		super(module);
	}

	public RepairKitOpenInventoryMessage(FriendlyByteBuf buffer)
	{
		super(buffer);
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);
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
	protected void toBuffer(FriendlyByteBuf buffer, IBuildingModule module)
	{
		super.toBuffer(buffer, module);
	}

}
