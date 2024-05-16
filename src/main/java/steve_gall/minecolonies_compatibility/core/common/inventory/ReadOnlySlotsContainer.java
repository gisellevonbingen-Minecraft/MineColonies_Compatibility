package steve_gall.minecolonies_compatibility.core.common.inventory;

import java.util.function.IntFunction;
import java.util.function.IntSupplier;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ReadOnlySlotsContainer implements Container
{
	private final IntSupplier sizeSupplier;
	private final IntFunction<ItemStack> stackFunction;

	public ReadOnlySlotsContainer(IntSupplier sizeSupplier, IntFunction<ItemStack> stackFunction)
	{
		this.sizeSupplier = sizeSupplier;
		this.stackFunction = stackFunction;
	}

	@Override
	public void clearContent()
	{

	}

	@Override
	public int getContainerSize()
	{
		return this.sizeSupplier.getAsInt();
	}

	@Override
	public boolean isEmpty()
	{
		for (var i = 0; i < this.getContainerSize(); i++)
		{
			if (!getItem(i).isEmpty())
			{
				return false;
			}

		}

		return true;
	}

	@Override
	public ItemStack getItem(int slot)
	{
		return slot < this.getContainerSize() ? this.stackFunction.apply(slot) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItem(int slot, int count)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int slot, ItemStack stack)
	{

	}

	@Override
	public void setChanged()
	{

	}

	@Override
	public boolean stillValid(Player player)
	{
		return false;
	}

}
