package steve_gall.minecolonies_compatibility.core.common.module.farmersdelight;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedCrop;

public class FarmersDelightModule
{
	public static void onLoad()
	{
		CustomizedCrop.register(new TomatoCrop());
	}

}
