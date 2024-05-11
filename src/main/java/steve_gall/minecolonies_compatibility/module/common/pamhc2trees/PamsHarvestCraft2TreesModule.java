package steve_gall.minecolonies_compatibility.module.common.pamhc2trees;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class PamsHarvestCraft2TreesModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new PamFruit());
	}

}
