package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableCrossbowItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.item.ModifiableSwordItem;
import steve_gall.minecolonies_compatibility.api.common.tool.CustomizedToolSystem;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

public class TConstructToolSystem extends CustomizedToolSystem
{
	public static final TConstructToolSystem INSTANCE = new TConstructToolSystem();

	private TConstructToolSystem()
	{

	}

	@Override
	public @NotNull ResourceLocation getId()
	{
		return MineColoniesCompatibility.rl("tconstruct");
	}

	@Override
	public boolean isTool(@NotNull ItemStack stack)
	{
		return stack.getItem() instanceof IModifiable;
	}

	@Override
	public boolean isSword(@NotNull ItemStack stack)
	{
		return stack.getItem() instanceof ModifiableSwordItem;
	}

	@Override
	public boolean isSpecialTool(@NotNull ItemStack stack, @NotNull IToolType toolType)
	{
		if (stack.getItem() instanceof ModifiableBowItem)
		{
			return toolType == ToolType.BOW;
		}
		else if (stack.getItem() instanceof ModifiableCrossbowItem)
		{
			return toolType == ModToolTypes.CROSSBOW.getToolType();
		}
		else if (stack.is(TinkerTools.flintAndBrick.get()))
		{
			return toolType == ToolType.FLINT_N_STEEL;
		}

		return false;
	}

	@Override
	public boolean isBroken(@NotNull ItemStack stack)
	{
		return ToolStack.from(stack).isBroken();
	}

	@Override
	public int getLevelUnclamped(@NotNull ItemStack stack)
	{
		var tool = ToolStack.from(stack);
		var materialVariants = TConstructToolHelper.getRepairVariants(tool);
		return materialVariants.stream().mapToInt(MaterialHelper::getRequiredLevel).max().orElse(-1);
	}

	@Override
	public double getAttackDamage(@NotNull ItemStack stack)
	{
		var tool = ToolStack.from(stack);
		return tool.getStats().get(ToolStats.ATTACK_DAMAGE).doubleValue();
	}

}
