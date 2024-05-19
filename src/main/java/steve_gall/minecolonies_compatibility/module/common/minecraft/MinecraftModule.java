package steve_gall.minecolonies_compatibility.module.common.minecraft;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class MinecraftModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new SweetBerryFruit());
		CustomizedFruit.register(new CocoaFruit());
	}

}
