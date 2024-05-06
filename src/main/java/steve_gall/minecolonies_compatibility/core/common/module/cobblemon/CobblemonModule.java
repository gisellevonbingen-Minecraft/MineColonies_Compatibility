package steve_gall.minecolonies_compatibility.core.common.module.cobblemon;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;

public class CobblemonModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new BerryFruit());
	}

}
