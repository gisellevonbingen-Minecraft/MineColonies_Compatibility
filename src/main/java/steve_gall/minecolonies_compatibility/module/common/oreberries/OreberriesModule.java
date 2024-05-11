package steve_gall.minecolonies_compatibility.module.common.oreberries;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class OreberriesModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new OreBerryFruit());
	}

}
