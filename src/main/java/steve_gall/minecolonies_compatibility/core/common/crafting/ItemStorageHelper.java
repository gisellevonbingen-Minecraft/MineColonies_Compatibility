package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.ItemStackUtils;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;

public class ItemStorageHelper
{
	public static List<ItemStack> getCraftingRemainings(List<ItemStorage> list)
	{
		return ItemStackHelper.getCraftingRemainings(list.stream().map(ItemStorage::getItemStack).toList());
	}

	public static List<List<ItemStack>> getStacksLists(List<ItemStorage> list)
	{
		return list.stream().map(ItemStorageHelper::getStacks).toList();
	}

	public static List<ItemStack> getStacks(ItemStorage storage)
	{
		return Collections.singletonList(storage.getItemStack());
	}

	public static boolean matches(ItemStorage storage, ItemStack stack, boolean min)
	{
		return ItemStackUtils.compareItemStacksIgnoreStackSize(storage.getItemStack(), stack, !storage.ignoreDamageValue(), !storage.ignoreNBT(), min, true);
	}

	private ItemStorageHelper()
	{

	}

}
