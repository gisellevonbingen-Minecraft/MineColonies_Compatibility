package steve_gall.minecolonies_compatibility.core.common.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class ContainerHelper
{
	public static List<ItemStack> getItemStacks(Container container)
	{
		return getItemStacks(container, 0, container.getContainerSize());
	}

	public static List<ItemStack> getItemStacks(Container container, int start, int end)
	{
		var list = new ArrayList<ItemStack>();

		for (var i = start; i < end; i++)
		{
			list.add(container.getItem(i));
		}

		return list;
	}

	private ContainerHelper()
	{

	}

}
