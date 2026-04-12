package steve_gall.minecolonies_compatibility.module.common.silentgear;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.util.constant.IToolType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.gear.api.item.ICoreWeapon;
import net.silentchaos512.gear.api.stats.ItemStats;
import net.silentchaos512.gear.item.gear.GearCrossbowItem;
import net.silentchaos512.gear.util.GearData;
import net.silentchaos512.gear.util.GearHelper;
import steve_gall.minecolonies_compatibility.api.common.tool.CustomizedToolSystem;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

public class SilentGearToolSystem extends CustomizedToolSystem
{
	public static final SilentGearToolSystem INSTANCE = new SilentGearToolSystem();

	@Override
	public @NotNull ResourceLocation getId()
	{
		return MineColoniesCompatibility.rl("silentgear");
	}

	@Override
	public boolean isTool(@NotNull ItemStack stack)
	{
		return GearHelper.isGear(stack);
	}

	@Override
	public boolean isSword(@NotNull ItemStack stack)
	{
		return stack.getItem() instanceof ICoreWeapon;
	}

	@Override
	public boolean isSpecialTool(@NotNull ItemStack stack, @NotNull IToolType toolType)
	{
		if (stack.getItem() instanceof GearCrossbowItem)
		{
			return toolType == ModToolTypes.CROSSBOW.getToolType();
		}

		return false;
	}

	@Override
	public boolean isBroken(@NotNull ItemStack stack)
	{
		return GearHelper.isBroken(stack);
	}

	@Override
	public int getLevelUnclamped(@NotNull ItemStack stack)
	{
		return GearData.getTier(stack);
	}

	@Override
	public float getAttackDamage(@NotNull ItemStack stack)
	{
		return GearData.getStat(stack, ItemStats.MELEE_DAMAGE);
	}

	private SilentGearToolSystem()
	{

	}

}
