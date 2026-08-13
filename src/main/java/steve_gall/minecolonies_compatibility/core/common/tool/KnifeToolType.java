package steve_gall.minecolonies_compatibility.core.common.tool;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.equipment.ModEquipmentTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import steve_gall.minecolonies_tweaks.api.common.tool.CustomToolType;

public class KnifeToolType extends CustomToolType
{
	public KnifeToolType(@NotNull ResourceLocation name)
	{
		super(name);
	}

	@Override
	public int getToolLevel(@NotNull ItemStack stack)
	{
		return ModEquipmentTypes.vanillaToolLevel(stack, this.getToolType());
	}

	@Override
	public boolean isTool(@NotNull ItemStack stack)
	{
		return stack.canPerformAction(ToolAction.get("knife_dig"));
	}

}
