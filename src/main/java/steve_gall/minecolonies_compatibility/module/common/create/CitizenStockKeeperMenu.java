package steve_gall.minecolonies_compatibility.module.common.create;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.inventory.BaseMenu;
import steve_gall.minecolonies_compatibility.module.common.create.init.ModuleMenuTypes;

public class CitizenStockKeeperMenu extends BaseMenu
{
	private final CitizenStockKeeperBlockEntity blockEntity;

	public CitizenStockKeeperMenu(int windowId, Inventory inventory, CitizenStockKeeperBlockEntity blockEntity)
	{
		super(ModuleMenuTypes.CITIZEN_STOCK_KEEPER.get(), windowId, inventory);
		this.blockEntity = blockEntity;
		
		this.setup();
	}

	public CitizenStockKeeperMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.CITIZEN_STOCK_KEEPER.get(), windowId, inventory);
		var pos = buffer.readBlockPos();
		this.blockEntity = (CitizenStockKeeperBlockEntity) inventory.player.level().getBlockEntity(pos);

		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(8, 65);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slot)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player)
	{
		return this.blockEntity != null && !this.blockEntity.isRemoved();
	}
	
	public CitizenStockKeeperBlockEntity getBlockEntity()
	{
		return this.blockEntity;
	}

}
