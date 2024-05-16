package steve_gall.minecolonies_compatibility.core.common.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

public abstract class ModuleMenu extends AbstractContainerMenu
{
	public static final int SLOT_OFFSET = 18;
	public static final int INVENTORY_COLUMNS = 9;
	public static final int INVENTORY_ROWS = 3;

	protected final Inventory inventory;
	protected final BlockPos buildingId;
	protected final int moduleId;

	protected ModuleMenu(MenuType<?> menuType, int windowId, Inventory inventory, BlockPos buildingId, int moduleId)
	{
		super(menuType, windowId);
		this.inventory = inventory;
		this.buildingId = buildingId;
		this.moduleId = moduleId;
	}

	protected ModuleMenu(MenuType<?> menuType, int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(menuType, windowId);
		this.inventory = inventory;
		this.buildingId = buffer.readBlockPos();
		this.moduleId = buffer.readInt();
	}

	protected void addInventorySlots(int initialX, int initialY)
	{
		for (var col = 0; col < INVENTORY_ROWS; col++)
		{
			for (var row = 0; row < INVENTORY_COLUMNS; row++)
			{
				var i = row + col * INVENTORY_COLUMNS + INVENTORY_COLUMNS;
				var x = initialX + row * SLOT_OFFSET;
				var y = initialY + col * SLOT_OFFSET;
				this.addSlot(new Slot(this.inventory, i, x, y));
			}

		}

		for (var col = 0; col < INVENTORY_COLUMNS; col++)
		{
			var x = initialX + col * SLOT_OFFSET;
			this.addSlot(new Slot(this.inventory, col, x, initialY + 58));
		}

	}

	public Inventory getInventory()
	{
		return this.inventory;
	}

	public BlockPos getBuildingId()
	{
		return this.buildingId;
	}

	public int getModuleId()
	{
		return this.moduleId;
	}

}
