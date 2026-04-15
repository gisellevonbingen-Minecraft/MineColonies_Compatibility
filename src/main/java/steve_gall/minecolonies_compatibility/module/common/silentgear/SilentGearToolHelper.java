package steve_gall.minecolonies_compatibility.module.common.silentgear;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.silentchaos512.gear.util.Const;
import steve_gall.minecolonies_compatibility.core.common.inventory.WrappingCraftingContainer;

public class SilentGearToolHelper
{
	public static boolean isToolAndBroken(ItemStack stack)
	{
		var system = SilentGearToolSystem.INSTANCE;
		return system.isTool(stack) && system.isBroken(stack);
	}

	public static boolean canRepair(ItemStack stack)
	{
		return stack.getDamageValue() > 0;
	}

	public static ItemStack fill(ItemStack repairKit, ItemStack material, Level level)
	{
		var recipe = (CraftingRecipe) level.getRecipeManager().byKey(Const.FILL_REPAIR_KIT).orElse(null);
		var container = new WrappingCraftingContainer(new RecipeWrapper(new ItemStackHandler(2)), 2, 1);
		container.setItem(0, repairKit);
		container.setItem(1, material);

		if (recipe.matches(container, level))
		{
			return recipe.assemble(container, level.registryAccess());
		}
		else
		{
			return repairKit;
		}

	}

	public static RepairResult repair(ItemStack tool, ItemStack repairKit, Level level)
	{
		var recipe = (CraftingRecipe) level.getRecipeManager().byKey(Const.QUICK_REPAIR).orElse(null);
		var container = new WrappingCraftingContainer(new RecipeWrapper(new ItemStackHandler(2)), 2, 1);
		container.setItem(0, tool);
		container.setItem(1, repairKit);

		if (recipe.matches(container, level))
		{
			var repaired = recipe.assemble(container, level.registryAccess());
			var remainingItems = recipe.getRemainingItems(container);
			return new RepairResult(true, repaired, remainingItems.get(1));
		}
		else
		{
			return new RepairResult(false, tool, repairKit);
		}

	}

	public record RepairResult(boolean success, ItemStack tool, ItemStack repairKit)
	{

	}

	public static RepairResult fillAndRepair(ItemStack tool, ItemStack repairKit, ItemStack material, Level level)
	{
		var filledRepairKit = fill(repairKit, material, level);
		return repair(tool, filledRepairKit, level);
	}

	public static int getRepairMaterialCount(ItemStack tool, ItemStack repairKit, ItemStack material, int limit, Level level)
	{
		var oldDamage = tool.getDamageValue();

		if (oldDamage == 0)
		{
			return 0;
		}

		var first = true;
		var usedCount = 0;
		tool = tool.copy();
		repairKit = repairKit.copy();

		while (true)
		{
			if (usedCount >= limit)
			{
				break;
			}

			var result = fillAndRepair(tool, repairKit, first ? ItemStack.EMPTY : material, level);

			if (!result.success())
			{
				break;
			}

			tool = result.tool();
			repairKit = result.repairKit();
			var newDamage = tool.getDamageValue();

			if (newDamage == 0)
			{
				if (!first)
				{
					usedCount++;
				}

				break;
			}
			else if (!first && oldDamage == newDamage)
			{
				break;
			}

			oldDamage = newDamage;

			if (!first)
			{
				usedCount++;
			}

			first = false;
		}

		return usedCount;
	}

}
