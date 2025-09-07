package steve_gall.minecolonies_compatibility.module.common.farmersdelight.network;

import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.ModuleMenuOpenMessage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.CuttingTeachMenu;

public class CuttingOpenTeachMessage extends ModuleMenuOpenMessage
{
	public static final CustomPacketPayload.Type<CuttingOpenTeachMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("farmerdelight_cutting_open_teach"));

	private final EquipmentTypeEntry toolType;

	public CuttingOpenTeachMessage(IBuildingModuleView module, EquipmentTypeEntry toolType)
	{
		super(module);

		this.toolType = toolType;
	}

	public CuttingOpenTeachMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.toolType = IMinecoloniesAPI.getInstance().getEquipmentTypeRegistry().get(buffer.readResourceLocation());
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeResourceLocation(IMinecoloniesAPI.getInstance().getEquipmentTypeRegistry().getKey(this.toolType));
	}

	@Override
	protected AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player, IBuildingModule module)
	{
		return new CuttingTeachMenu(windowId, inventory, module, this.toolType);
	}

	@Override
	protected void toBuffer(RegistryFriendlyByteBuf buffer, IBuildingModule module)
	{
		super.toBuffer(buffer, module);

		buffer.writeResourceLocation(IMinecoloniesAPI.getInstance().getEquipmentTypeRegistry().getKey(this.getToolType()));
	}

	@Override
	public CustomPacketPayload.Type<CuttingOpenTeachMessage> type()
	{
		return TYPE;
	}

	public EquipmentTypeEntry getToolType()
	{
		return this.toolType;
	}

}
