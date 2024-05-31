package steve_gall.minecolonies_compatibility.core.common.init;

import java.util.Arrays;

import com.minecolonies.api.util.constant.ToolType;

import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.tool.CustomToolType;
import steve_gall.minecolonies_tweaks.api.common.tool.OrToolType;

public class ModToolTypes
{
	public static final CustomToolType CROSSBOW = new CustomToolType(MineColoniesCompatibility.rl("crossbow"));
	public static final CustomToolType BOW_LIKE = new OrToolType(MineColoniesCompatibility.rl("bow_like"), Arrays.asList(() -> ToolType.BOW, CROSSBOW::getToolType));
	public static final CustomToolType GUN = new CustomToolType(MineColoniesCompatibility.rl("gun"));
	public static final CustomToolType KNIFE = new CustomToolType(MineColoniesCompatibility.rl("knife"));

}
