package steve_gall.minecolonies_compatibility.module.common.delightful;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class DelightfulModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new SalmonberryFruit());
	}

}
