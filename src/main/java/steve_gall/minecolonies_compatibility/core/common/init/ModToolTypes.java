package steve_gall.minecolonies_compatibility.core.common.init;

import java.util.Arrays;

import com.minecolonies.api.equipment.ModEquipmentTypes;

import steve_gall.minecolonies_compatibility.core.common.tool.KnightWeaponToolType;
import steve_gall.minecolonies_compatibility.core.common.tool.RangerWeaponToolType;
import steve_gall.minecolonies_tweaks.api.common.tool.CustomToolType;
import steve_gall.minecolonies_tweaks.core.common.MineColoniesTweaks;

public class ModToolTypes
{
	public static final CustomToolType CROSSBOW = new CustomToolType(MineColoniesTweaks.rl("crossbow"));
	public static final CustomToolType GUN = new CustomToolType(MineColoniesTweaks.rl("gun"));
	public static final CustomToolType KNIFE = new CustomToolType(MineColoniesTweaks.rl("knife"));

	public static final RangerWeaponToolType RANGER_WEAPON = new RangerWeaponToolType(MineColoniesTweaks.rl("ranger_weapon"), Arrays.asList(ModEquipmentTypes.bow::get));
	public static final KnightWeaponToolType KNIGHT_WEAPON = new KnightWeaponToolType(MineColoniesTweaks.rl("knight_weapon"), Arrays.asList(ModEquipmentTypes.sword::get));
}
