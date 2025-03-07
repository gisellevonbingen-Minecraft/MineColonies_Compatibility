package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableCrossbowItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerTools;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

public class ProxyMethods
{
	public static boolean isSpecialTool(ItemStack stack, IToolType toolType)
	{
		if (stack.getItem() instanceof ModifiableBowItem)
		{
			return toolType == ToolType.BOW;
		}
		else if (stack.getItem() instanceof ModifiableCrossbowItem)
		{
			return toolType == ModToolTypes.CROSSBOW.getToolType();
		}
		else if (stack.getItem() instanceof ModifiableArmorItem armor)
		{
			var slot = armor.getSlot();

			if (slot == EquipmentSlot.HEAD)
			{
				return toolType == ToolType.HELMET;
			}
			else if (slot == EquipmentSlot.CHEST)
			{
				return toolType == ToolType.CHESTPLATE;
			}
			else if (slot == EquipmentSlot.LEGS)
			{
				return toolType == ToolType.LEGGINGS;
			}
			else if (slot == EquipmentSlot.FEET)
			{
				return toolType == ToolType.BOOTS;
			}

		}
		else if (stack.is(TinkerTools.flintAndBrick.get()))
		{
			return toolType == ToolType.FLINT_N_STEEL;
		}

		return false;
	}

	public static double getAttackDamage(ItemStack stack)
	{
		if (stack.getItem() instanceof ModifiableItem)
		{
			var tool = ToolStack.from(stack);
			return tool.getStats().get(ToolStats.ATTACK_DAMAGE).doubleValue();
		}

		return -1.0D;
	}

	private ProxyMethods()
	{

	}

}
