package steve_gall.minecolonies_compatibility.module.common.silentgear.network;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.ModuleMenuOpenMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.menu.RepairMaterialTeachMenu;

public class RepairMaterialOpenTeachMessage extends ModuleMenuOpenMessage
{
	public static final CustomPacketPayload.Type<RepairMaterialOpenTeachMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("silentgear_repair_material_open_teach"));

	public RepairMaterialOpenTeachMessage(IBuildingModuleView module)
	{
		super(module);
	}

	public RepairMaterialOpenTeachMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);
	}

	@Override
	public CustomPacketPayload.Type<RepairMaterialOpenTeachMessage> type()
	{
		return TYPE;
	}

	@Override
	protected AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player, IBuildingModule module)
	{
		return new RepairMaterialTeachMenu(windowId, inventory, module);
	}

	@Override
	protected void toBuffer(RegistryFriendlyByteBuf buffer, IBuildingModule module)
	{
		super.toBuffer(buffer, module);

		buffer.writeInt(module.getBuilding().getBuildingLevel());
	}

}
