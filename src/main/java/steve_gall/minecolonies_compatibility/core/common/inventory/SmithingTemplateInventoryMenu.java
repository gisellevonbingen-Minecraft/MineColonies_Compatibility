package steve_gall.minecolonies_compatibility.core.common.inventory;

import org.jetbrains.annotations.NotNull;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import steve_gall.minecolonies_compatibility.core.common.building.module.SmithingTemplateCraftingModule;
import steve_gall.minecolonies_compatibility.core.common.init.ModMenuTypes;

public class SmithingTemplateInventoryMenu extends ModuleMenu
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 86;

	private final IItemHandlerModifiable inventory;

	public SmithingTemplateInventoryMenu(int windowId, Inventory inventory, SmithingTemplateCraftingModule module)
	{
		super(ModMenuTypes.SMITHING_TEMPLATE_INVENTORY.get(), windowId, inventory, module);

		this.inventory = module.getInventory();
		this.setup();
	}

	public SmithingTemplateInventoryMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModMenuTypes.SMITHING_TEMPLATE_INVENTORY.get(), windowId, inventory, buffer);

		this.inventory = new ItemStackHandler(SmithingTemplateCraftingModule.INVENTORY_SLOTS);
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
