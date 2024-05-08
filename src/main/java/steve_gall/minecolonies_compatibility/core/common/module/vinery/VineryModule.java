package steve_gall.minecolonies_compatibility.core.common.module.vinery;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;

public class VineryModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new AppleLeavesFruit());
		CustomizedFruit.register(new CherryLeavesFruit());
		CustomizedFruit.register(new GrapeBushFruit());
		CustomizedFruit.register(new GrapeVineFruit());
	}

}
