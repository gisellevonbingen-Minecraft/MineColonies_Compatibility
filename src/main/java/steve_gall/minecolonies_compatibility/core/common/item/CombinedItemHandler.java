package steve_gall.minecolonies_compatibility.core.common.item;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class CombinedItemHandler implements IItemHandler
{
	private final IItemHandler[] handlers;
	private final int[] offsets;
	private final int slots;

	public CombinedItemHandler(IItemHandler[] handlers)
	{
		var offset = 0;
		this.handlers = new IItemHandler[handlers.length];
		this.offsets = new int[handlers.length];

		for (var i = 0; i < handlers.length; i++)
		{
			var handler = handlers[i];
			this.handlers[i] = handler;
			this.offsets[i] = offset;

			offset += handler.getSlots();
		}

		this.slots = offset;
	}

	@Override
	public int getSlots()
	{
		return this.slots;
	}

	private int getHandlerIndex(int slot)
	{
		if (0 <= slot && slot < this.slots)
		{
			for (var i = 0; i < this.offsets.length; i++)
			{
				if (slot < this.offsets[i])
				{
					return i - 1;
				}

			}

			return this.offsets.length - 1;
		}
		else
		{
			return -1;
		}

	}

	public InternalHandlerInfo getInternalHandler(int slot)
	{
		var handlerIndex = this.getHandlerIndex(slot);
		return handlerIndex == -1 ? null : new InternalHandlerInfo(this.handlers[handlerIndex], slot - this.offsets[handlerIndex]);
	}

	@Override
	public @NotNull ItemStack getStackInSlot(int slot)
	{
		var internal = this.getInternalHandler(slot);
		return internal == null ? ItemStack.EMPTY : internal.getStackInSlot();
	}

	@Override
	public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
	{
		var internal = this.getInternalHandler(slot);
		return internal == null ? stack : internal.insertItem(stack, simulate);
	}

	@Override
	public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		var internal = this.getInternalHandler(slot);
		return internal == null ? ItemStack.EMPTY : internal.extractItem(amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot)
	{
		var internal = this.getInternalHandler(slot);
		return internal == null ? 0 : internal.getSlotLimit();
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack)
	{
		var internal = this.getInternalHandler(slot);
		return internal == null ? false : internal.isItemValid(stack);
	}

	public static record InternalHandlerInfo(IItemHandler handler, int slot)
	{
		public @NotNull ItemStack getStackInSlot()
		{
			return this.handler.getStackInSlot(this.slot);
		}

		public @NotNull ItemStack insertItem(@NotNull ItemStack stack, boolean simulate)
		{
			return this.handler.insertItem(this.slot, stack, simulate);
		}

		public @NotNull ItemStack extractItem(int amount, boolean simulate)
		{
			return this.handler.extractItem(this.slot, amount, simulate);
		}

		public int getSlotLimit()
		{
			return this.handler.getSlotLimit(this.slot);
		}

		public boolean isItemValid(@NotNull ItemStack stack)
		{
			return this.handler.isItemValid(this.slot, stack);
		}

	}

}
