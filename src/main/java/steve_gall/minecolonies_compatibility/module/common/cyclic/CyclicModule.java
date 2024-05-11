package steve_gall.minecolonies_compatibility.module.common.cyclic;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class CyclicModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new AppleSproutFruit());
	}

}
