package steve_gall.minecolonies_compatibility.core.common.module.undergarden;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class UndergardenModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new UnderbeanFruit());
		CustomizedFruit.register(new BlisterberryFruit());
	}

}
