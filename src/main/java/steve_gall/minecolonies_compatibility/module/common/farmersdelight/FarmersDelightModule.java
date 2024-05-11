package steve_gall.minecolonies_compatibility.module.common.farmersdelight;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;

public class FarmersDelightModule
{
	public static void onLoad()
	{
		CustomizedCrop.register(new TomatoCrop());
	}

}
