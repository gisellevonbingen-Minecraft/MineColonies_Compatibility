package steve_gall.minecolonies_compatibility.module.common.storagenetwork;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.inventory.BaseMenu;
import steve_gall.minecolonies_compatibility.module.common.storagenetwork.init.ModuleMenuTypes;

public class CitizenInventoryMenu extends BaseMenu
{
	private final CitizenInventoryBlockEntity blockEntity;

	public CitizenInventoryMenu(int windowId, Inventory inventory, CitizenInventoryBlockEntity blockEntity)
	{
		super(ModuleMenuTypes.CITIZEN_INVENTORY.get(), windowId, inventory);
		this.blockEntity = blockEntity;

		this.setup();
	}

	public CitizenInventoryMenu(int windowId, Inventory inventory, FriendlyByteBuf data)
	{
		super(ModuleMenuTypes.CITIZEN_INVENTORY.get(), windowId, inventory);

		var pos = data.readBlockPos();
		this.blockEntity = (CitizenInventoryBlockEntity) inventory.player.level().getBlockEntity(pos);

		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(8, 55);
	}

	public CitizenInventoryBlockEntity getBlockEntity()
	{
		return this.blockEntity;
	}

	@Override
	public ItemStack quickMoveStack(Player p_38941_, int p_38942_)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player p_38874_)
	{
		return true;
	}

}
