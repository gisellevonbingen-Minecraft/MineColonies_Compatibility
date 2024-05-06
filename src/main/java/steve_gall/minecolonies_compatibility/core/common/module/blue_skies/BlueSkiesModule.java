package steve_gall.minecolonies_compatibility.core.common.module.blue_skies;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;

public class BlueSkiesModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new BrewBerryFruit());
	}

}
