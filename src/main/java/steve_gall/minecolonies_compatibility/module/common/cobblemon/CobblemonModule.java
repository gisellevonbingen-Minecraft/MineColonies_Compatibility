package steve_gall.minecolonies_compatibility.module.common.cobblemon;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class CobblemonModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new BerryFruit());
	}

}
