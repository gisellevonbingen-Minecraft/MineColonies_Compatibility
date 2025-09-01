package steve_gall.minecolonies_compatibility.core.common.tool;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.equipment.ModEquipmentTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import steve_gall.minecolonies_tweaks.api.common.tool.CustomToolType;

public class CrossBowToolType extends CustomToolType
{
	public CrossBowToolType(@NotNull ResourceLocation name)
	{
		super(name);
	}

	@Override
	public boolean isTool(@NotNull ItemStack stack)
	{
		return stack.getItem() instanceof CrossbowItem;
	}

	@Override
	public int getToolLevel(@NotNull ItemStack stack)
	{
		return ModEquipmentTypes.durabilityBasedLevel(stack, Items.CROSSBOW.getMaxDamage(stack));
	}

}
