package steve_gall.minecolonies_compatibility.core.common.module.minecraft;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;

public class MinecraftModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new SweetBerryFruit());
	}

}
