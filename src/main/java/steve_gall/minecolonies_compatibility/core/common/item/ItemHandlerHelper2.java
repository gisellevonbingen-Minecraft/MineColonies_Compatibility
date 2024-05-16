package steve_gall.minecolonies_compatibility.core.common.item;

import java.util.ArrayList;
import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import steve_gall.minecolonies_compatibility.core.common.crafting.ItemStorageHelper;

public class ItemHandlerHelper2
{
	public static IItemHandlerModifiable wrap(List<ItemStack> list)
	{
		var handler = new ItemStackHandler(list.size());

		for (var i = 0; i < handler.getSlots(); i++)
		{
			handler.setStackInSlot(i, list.get(i));
		}

		return handler;
	}

	public static List<ItemStack> unwrap(IItemHandler handler, boolean empty)
	{
		var slots = handler.getSlots();
		var list = new ArrayList<ItemStack>(slots);

		for (var i = 0; i < slots; i++)
		{
			var stack = handler.getStackInSlot(i);

			if (empty || !stack.isEmpty())
			{
				list.add(stack);
			}

		}

		return list;
	}

	public static boolean move(IItemHandler from, IItemHandlerModifiable to, int toIndexStart, List<ItemStorage> storages)
	{
		for (int storageIndex = 0; storageIndex < storages.size(); storageIndex++)
		{
			var storage = storages.get(storageIndex);
			var inserted = false;

			for (var fromSlot = 0; fromSlot < from.getSlots(); fromSlot++)
			{
				if (!to.getStackInSlot(toIndexStart + storageIndex).isEmpty())
				{
					return false;
				}

				var extracting = from.extractItem(fromSlot, storage.getAmount(), true);

				if (ItemStorageHelper.matches(storage, extracting, true))
				{
					to.setStackInSlot(toIndexStart + storageIndex, extracting.copy());
					from.extractItem(fromSlot, storage.getAmount(), false);
					inserted = true;
					break;
				}

			}

			if (!inserted)
			{
				return false;
			}

		}

		return true;
	}

	private ItemHandlerHelper2()
	{

	}

}
