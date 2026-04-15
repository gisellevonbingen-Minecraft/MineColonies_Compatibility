package steve_gall.minecolonies_compatibility.core.common.inventory;

import java.util.List;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

public class WrappingCraftingContainer implements CraftingContainer
{
	protected final Container parent;
	protected final int width;
	protected final int height;

	public WrappingCraftingContainer(CraftingContainer parent)
	{
		this(parent, parent.getWidth(), parent.getHeight());
	}

	public WrappingCraftingContainer(Container parent, int width, int height)
	{
		this.parent = parent;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getContainerSize()
	{
		return this.parent.getContainerSize();
	}

	@Override
	public boolean isEmpty()
	{
		return this.parent.isEmpty();
	}

	@Override
	public ItemStack getItem(int slot)
	{
		return this.parent.getItem(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount)
	{
		return this.parent.removeItem(slot, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot)
	{
		return this.parent.removeItemNoUpdate(slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack)
	{
		this.parent.setItem(slot, stack);
	}

	@Override
	public void setChanged()
	{
		this.parent.setChanged();
	}

	@Override
	public boolean stillValid(Player player)
	{
		return this.parent.stillValid(player);
	}

	@Override
	public void clearContent()
	{
		this.parent.clearContent();
	}

	@Override
	public void fillStackedContents(StackedContents contents)
	{
		if (this.parent instanceof CraftingContainer parent)
		{
			parent.fillStackedContents(contents);
		}
		else
		{
			var size = this.parent.getContainerSize();

			for (var i = 0; i < size; i++)
			{
				contents.accountSimpleStack(this.parent.getItem(i));
			}

		}

	}

	@Override
	public int getWidth()
	{
		if (this.parent instanceof CraftingContainer parent)
		{
			return parent.getWidth();
		}
		else
		{
			return this.width;
		}

	}

	@Override
	public int getHeight()
	{
		if (this.parent instanceof CraftingContainer parent)
		{
			return parent.getHeight();
		}
		else
		{
			return this.height;
		}

	}

	@Override
	public List<ItemStack> getItems()
	{
		if (this.parent instanceof CraftingContainer parent)
		{
			return parent.getItems();
		}
		else
		{
			var list = NonNullList.withSize(this.parent.getContainerSize(), ItemStack.EMPTY);

			for (var i = 0; i < list.size(); i++)
			{
				list.set(i, this.parent.getItem(i));
			}

			return list;
		}

	}

}
