package steve_gall.minecolonies_compatibility.core.common.inventory;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class InventoryHelper
{
	public static int removeStacksFromItemHandler(@NotNull IItemHandler inventory, int count, @NotNull Predicate<ItemStack> predicate)
	{
		var remainedCount = Math.max(count, 0);

		for (var i = 0; remainedCount > 0 && i < inventory.getSlots(); i++)
		{
			var item = inventory.getStackInSlot(i);

			if (predicate.test(item))
			{
				var reduce = Math.min(remainedCount, item.getCount());
				item.shrink(reduce);
				remainedCount -= reduce;
			}

		}

		return remainedCount;
	}

	private InventoryHelper()
	{

	}

}
