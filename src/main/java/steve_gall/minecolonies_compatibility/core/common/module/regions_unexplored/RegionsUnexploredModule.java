package steve_gall.minecolonies_compatibility.core.common.module.regions_unexplored;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class RegionsUnexploredModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new AppleLeavesFruit());
		CustomizedFruit.register(new SalmonBerryFruit());
	}

}
