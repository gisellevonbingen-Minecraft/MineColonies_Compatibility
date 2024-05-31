package steve_gall.minecolonies_compatibility.core.common.init;

import java.util.Arrays;

import com.minecolonies.api.util.constant.ToolType;

import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.tool.KnightWeaponToolType;
import steve_gall.minecolonies_compatibility.core.common.tool.RangerWeaponToolType;
import steve_gall.minecolonies_tweaks.api.common.tool.CustomToolType;

public class ModToolTypes
{
	public static final CustomToolType CROSSBOW = new CustomToolType(MineColoniesCompatibility.rl("crossbow"));
	public static final CustomToolType GUN = new CustomToolType(MineColoniesCompatibility.rl("gun"));
	public static final CustomToolType KNIFE = new CustomToolType(MineColoniesCompatibility.rl("knife"));

	public static final RangerWeaponToolType RANGER_WEAPON = new RangerWeaponToolType(MineColoniesCompatibility.rl("ranger_weapon"), Arrays.asList(() -> ToolType.BOW));
	public static final KnightWeaponToolType KNIGHT_WEAPON = new KnightWeaponToolType(MineColoniesCompatibility.rl("knight_weapon"), Arrays.asList(() -> ToolType.SWORD));
}
