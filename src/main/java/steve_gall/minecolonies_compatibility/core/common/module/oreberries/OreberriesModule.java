package steve_gall.minecolonies_compatibility.core.common.module.oreberries;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class OreberriesModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new OreBerryFruit());
	}

}
