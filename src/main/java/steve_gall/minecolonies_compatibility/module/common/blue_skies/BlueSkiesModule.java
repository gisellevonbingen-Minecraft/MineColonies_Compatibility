package steve_gall.minecolonies_compatibility.module.common.blue_skies;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class BlueSkiesModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new BrewBerryFruit());
	}

}
