package steve_gall.minecolonies_compatibility.core.common.item;

import java.util.function.Supplier;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class LazyWrappedItemHandler implements IItemHandler
{
	private final Supplier<IItemHandler> parent;

	public LazyWrappedItemHandler(Supplier<IItemHandler> parent)
	{
		this.parent = parent;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack)
	{
		return this.parent.get().isItemValid(slot, stack);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		return this.parent.get().insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return this.parent.get().extractItem(slot, amount, simulate);
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.parent.get().getStackInSlot(slot);
	}

	@Override
	public int getSlots()
	{
		return this.parent.get().getSlots();
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return this.parent.get().getSlotLimit(slot);
	}

}
