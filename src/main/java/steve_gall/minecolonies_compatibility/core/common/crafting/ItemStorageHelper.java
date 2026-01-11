package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.ItemStackUtils;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;

public class ItemStorageHelper
{
	public static List<ItemStack> getCraftingRemainings(List<ItemStorage> list)
	{
		return ItemStackHelper.getCraftingRemainings(list.stream().map(ItemStorageHelper::getAmountedStack).toList());
	}

	public static List<ItemStack> mapAndFilterNotEmpty(List<ItemStorage> list, Function<ItemStack, ItemStack> func)
	{
		return ItemStackHelper.mapAndFilterNotEmpty(list.stream().map(ItemStorageHelper::getAmountedStack).toList(), func);
	}

	public static List<ItemStorage> filterNotEmpty(List<ItemStorage> inputs)
	{
		return inputs.stream().filter(storage -> !storage.isEmpty()).toList();
	}

	public static List<List<ItemStack>> getAmountedStacksLists(List<ItemStorage> list)
	{
		return list.stream().map(ItemStorageHelper::getAmountedStacks).toList();
	}

	public static List<ItemStack> getAmountedStacks(ItemStorage storage)
	{
		return Collections.singletonList(getAmountedStack(storage));
	}

	public static ItemStack getAmountedStack(ItemStorage storage)
	{
		return storage.getItemStack().copyWithCount(storage.getAmount());
	}

	public static boolean matches(ItemStorage storage, ItemStack stack, boolean min)
	{
		return ItemStackUtils.compareItemStacksIgnoreStackSize(storage.getItemStack(), stack, !storage.ignoreDamageValue(), !storage.ignoreNBT(), min, true);
	}

	private ItemStorageHelper()
	{

	}

}
