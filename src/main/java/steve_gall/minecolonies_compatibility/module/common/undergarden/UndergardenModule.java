package steve_gall.minecolonies_compatibility.module.common.undergarden;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class UndergardenModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new UnderbeanFruit());
		CustomizedFruit.register(new BlisterberryFruit());
	}

}
