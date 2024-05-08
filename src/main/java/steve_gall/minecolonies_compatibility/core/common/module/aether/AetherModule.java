package steve_gall.minecolonies_compatibility.core.common.module.aether;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class AetherModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new BlueBerryFruit());
	}

}
