package steve_gall.minecolonies_compatibility.core.common.init;

import java.util.Arrays;

import com.minecolonies.api.equipment.ModEquipmentTypes;

import steve_gall.minecolonies_compatibility.core.common.tool.GunToolType;
import steve_gall.minecolonies_compatibility.core.common.tool.KnifeToolType;
import steve_gall.minecolonies_tweaks.api.common.tool.OrToolType;
import steve_gall.minecolonies_tweaks.core.common.MineColoniesTweaks;

public class ModToolTypes
{
	public static final GunToolType GUN = new GunToolType(MineColoniesTweaks.rl("gun"));
	public static final KnifeToolType KNIFE = new KnifeToolType(MineColoniesTweaks.rl("knife"));

	public static final OrToolType BUTCHER_TOOL = new OrToolType(MineColoniesTweaks.rl("butcher_tool"), Arrays.asList(ModEquipmentTypes.axe::get, KNIFE::getToolType));
}
