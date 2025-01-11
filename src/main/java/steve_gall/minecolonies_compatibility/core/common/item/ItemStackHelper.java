package steve_gall.minecolonies_compatibility.core.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.world.item.ItemStack;

public class ItemStackHelper
{
	public static List<ItemStack> getCraftingRemainings(List<ItemStack> list)
	{
		return mapAndFilterNotEmpty(list, ItemStack::getCraftingRemainingItem);
	}

	public static List<ItemStack> mapAndFilterNotEmpty(List<ItemStack> inputs, Function<ItemStack, ItemStack> func)
	{
		var outputs = new ArrayList<ItemStack>();

		for (var input : inputs)
		{
			var output = func.apply(input);

			if (!output.isEmpty())
			{
				outputs.add(output);
			}

		}

		return outputs;
	}

	public static List<ItemStack> filterNotEmpty(List<ItemStack> inputs)
	{
		return inputs.stream().filter(stack -> !stack.isEmpty()).toList();
	}

	public static boolean isTool(@NotNull ItemStack stack, @NotNull IToolType toolType)
	{
		return ItemStackUtils.isTool(stack, toolType);
	}

	public static @NotNull ToolType getFirstToolType(@NotNull ItemStack stack)
	{
		for (var toolType : ToolType.values())
		{
			if (isTool(stack, toolType))
			{
				return toolType;
			}

		}

		return ToolType.NONE;
	}

	public static boolean equals(ItemStack stack1, ItemStack stack2)
	{
		return equalsIgnoreSize(stack1, stack2) && stack1.getCount() == stack2.getCount();
	}

	public static boolean equalsIgnoreSize(ItemStack stack1, ItemStack stack2)
	{
		return ItemStackUtils.compareItemStacksIgnoreStackSize(stack1, stack2);
	}

	private ItemStackHelper()
	{

	}

}
