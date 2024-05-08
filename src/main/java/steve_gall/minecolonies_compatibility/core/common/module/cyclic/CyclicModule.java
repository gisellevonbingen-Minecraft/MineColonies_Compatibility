package steve_gall.minecolonies_compatibility.core.common.module.cyclic;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;

public class CyclicModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new AppleSproutFruit());
	}

}
