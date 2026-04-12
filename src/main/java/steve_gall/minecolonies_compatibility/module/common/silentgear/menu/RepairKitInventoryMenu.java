package steve_gall.minecolonies_compatibility.module.common.silentgear.menu;

import org.jetbrains.annotations.NotNull;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import steve_gall.minecolonies_compatibility.core.common.inventory.ModuleMenu;
import steve_gall.minecolonies_compatibility.module.common.silentgear.building.module.RepairMaterialListModule;
import steve_gall.minecolonies_compatibility.module.common.silentgear.init.ModuleMenuTypes;

public class RepairKitInventoryMenu extends ModuleMenu
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 86;

	private final IItemHandlerModifiable inventory;

	public RepairKitInventoryMenu(int windowId, Inventory inventory, RepairMaterialListModule module)
	{
		super(ModuleMenuTypes.REPAIR_KIT_INVENTORY.get(), windowId, inventory, module);

		this.inventory = module.getRepairKitInventory();
		this.setup();
	}

	public RepairKitInventoryMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.REPAIR_KIT_INVENTORY.get(), windowId, inventory, buffer);

		this.inventory = new ItemStackHandler(RepairMaterialListModule.INVENTORY_SLOTS);
		this.setup();
	}

	private void setup()
	{
		for (var i = 0; i < this.inventory.getSlots(); i++)
		{
			var xi = i % 9;
			var yi = i / 9;
			this.addSlot(new SlotItemHandler(this.inventory, i, 8 + SLOT_OFFSET * xi, 18 + SLOT_OFFSET * yi)
			{
				@Override
				public int getMaxStackSize(@NotNull ItemStack stack)
				{
					return Math.min(this.getMaxStackSize(), stack.getMaxStackSize());
				}
			});
		}

		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotNumber)
	{
		var itemstack = ItemStack.EMPTY;
		var slot = this.slots.get(slotNumber);

		if (slot != null && slot.hasItem())
		{
			var inventorySlots = this.inventory.getSlots();
			var itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (slotNumber < inventorySlots)
			{
				if (!this.moveItemStackTo(itemstack1, inventorySlots, this.slots.size(), true))
				{
					return ItemStack.EMPTY;
				}

			}
			else if (!this.moveItemStackTo(itemstack1, 0, inventorySlots, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
			{
				slot.setByPlayer(ItemStack.EMPTY);
			}
			else
			{
				slot.setChanged();
			}

		}

		return itemstack;
	}

	@Override
	public boolean stillValid(Player player)
	{
		return true;
	}

	public IItemHandlerModifiable getModuleInventory()
	{
		return this.inventory;
	}

}
