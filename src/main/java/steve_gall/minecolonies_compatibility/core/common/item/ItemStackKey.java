package steve_gall.minecolonies_compatibility.core.common.item;

import java.util.Objects;

import net.minecraft.world.item.ItemStack;

public class ItemStackKey
{
	private final ItemStack stack;
	private final int hashCode;

	public ItemStackKey(ItemStack stack)
	{
		this.stack = stack.copy();
		this.hashCode = Objects.hash(stack.getItem(), stack.getComponents().toString());
	}

	@Override
	public String toString()
	{
		return this.stack.toString();
	}

	@Override
	public int hashCode()
	{
		return this.hashCode;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		else if (obj == null)
		{
			return false;
		}
		else
		{
			return obj instanceof ItemStackKey other && ItemStack.isSameItemSameComponents(this.stack, other.stack);
		}

	}

	public ItemStack getStack(int count)
	{
		var stack = this.stack.copy();
		stack.setCount(count);
		return stack;
	}

	public ItemStack getStack(long count)
	{
		return this.getStack((int) Math.min(count, Integer.MAX_VALUE));
	}

}
