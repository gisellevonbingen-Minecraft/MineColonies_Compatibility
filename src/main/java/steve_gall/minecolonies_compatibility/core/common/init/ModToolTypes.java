package steve_gall.minecolonies_compatibility.core.common.init;

import java.util.Arrays;

import com.minecolonies.api.equipment.ModEquipmentTypes;

import steve_gall.minecolonies_compatibility.core.common.tool.CrossBowToolType;
import steve_gall.minecolonies_compatibility.core.common.tool.GunToolType;
import steve_gall.minecolonies_compatibility.core.common.tool.KnifeToolType;
import steve_gall.minecolonies_compatibility.core.common.tool.KnightWeaponToolType;
import steve_gall.minecolonies_compatibility.core.common.tool.RangerWeaponToolType;
import steve_gall.minecolonies_tweaks.api.common.tool.OrToolType;
import steve_gall.minecolonies_tweaks.core.common.MineColoniesTweaks;

public class ModToolTypes
{
	public static final CrossBowToolType CROSSBOW = new CrossBowToolType(MineColoniesTweaks.rl("crossbow"));
	public static final GunToolType GUN = new GunToolType(MineColoniesTweaks.rl("gun"));
	public static final KnifeToolType KNIFE = new KnifeToolType(MineColoniesTweaks.rl("knife"));

	public static final RangerWeaponToolType RANGER_WEAPON = new RangerWeaponToolType(MineColoniesTweaks.rl("ranger_weapon"), Arrays.asList(ModEquipmentTypes.bow::get));
	public static final KnightWeaponToolType KNIGHT_WEAPON = new KnightWeaponToolType(MineColoniesTweaks.rl("knight_weapon"), Arrays.asList(ModEquipmentTypes.sword::get));
	public static final OrToolType BUTCHER_TOOL = new OrToolType(MineColoniesTweaks.rl("butcher_tool"), Arrays.asList(ModEquipmentTypes.axe::get, KNIFE::getToolType));
}
