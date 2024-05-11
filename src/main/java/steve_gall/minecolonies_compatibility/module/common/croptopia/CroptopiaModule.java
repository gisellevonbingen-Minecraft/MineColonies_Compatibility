package steve_gall.minecolonies_compatibility.module.common.croptopia;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class CroptopiaModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new LeafCropFruit());
	}

}
