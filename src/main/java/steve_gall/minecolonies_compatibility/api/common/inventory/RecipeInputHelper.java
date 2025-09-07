package steve_gall.minecolonies_compatibility.api.common.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class RecipeInputHelper
{
	public static List<ItemStack> getItemStacks(RecipeInput input)
	{
		var list = new ArrayList<ItemStack>();

		for (var i = 0; i < input.size(); i++)
		{
			list.add(input.getItem(i));
		}

		return list;
	}

	private RecipeInputHelper()
	{

	}

}
