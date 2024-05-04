package steve_gall.minecolonies_compatibility.core.common.module.pamhc2trees;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;

public class PamsHarvestCraft2TreesModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new PamFruit());
	}

}
