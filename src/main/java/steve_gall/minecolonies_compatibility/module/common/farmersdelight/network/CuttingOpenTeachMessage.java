package steve_gall.minecolonies_compatibility.module.common.farmersdelight.network;

import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import steve_gall.minecolonies_compatibility.core.common.network.message.ModuleMenuOpenMessage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.CuttingTeachMenu;

public class CuttingOpenTeachMessage extends ModuleMenuOpenMessage
{
	private final EquipmentTypeEntry toolType;

	public CuttingOpenTeachMessage(IBuildingModuleView module, EquipmentTypeEntry toolType)
	{
		super(module);

		this.toolType = toolType;
	}

	public CuttingOpenTeachMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.toolType = buffer.readRegistryIdUnsafe(IMinecoloniesAPI.getInstance().getEquipmentTypeRegistry());
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeRegistryIdUnsafe(IMinecoloniesAPI.getInstance().getEquipmentTypeRegistry(), this.toolType);
	}

	@Override
	protected AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player, IBuildingModule module)
	{
		return new CuttingTeachMenu(windowId, inventory, module, this.toolType);
	}

	@Override
	protected void toBuffer(FriendlyByteBuf buffer, IBuildingModule module)
	{
		super.toBuffer(buffer, module);

		buffer.writeRegistryIdUnsafe(IMinecoloniesAPI.getInstance().getEquipmentTypeRegistry(), this.getToolType());
	}

	public EquipmentTypeEntry getToolType()
	{
		return this.toolType;
	}

}
