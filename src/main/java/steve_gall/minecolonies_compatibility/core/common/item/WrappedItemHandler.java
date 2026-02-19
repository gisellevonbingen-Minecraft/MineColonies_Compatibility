package steve_gall.minecolonies_compatibility.core.common.item;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class WrappedItemHandler implements IItemHandler
{
	private final IItemHandler parent;

	public WrappedItemHandler(IItemHandler parent)
	{
		this.parent = parent;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack)
	{
		return this.parent.isItemValid(slot, stack);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		return this.parent.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return this.parent.extractItem(slot, amount, simulate);
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.parent.getStackInSlot(slot);
	}

	@Override
	public int getSlots()
	{
		return this.parent.getSlots();
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return this.parent.getSlotLimit(slot);
	}

}
