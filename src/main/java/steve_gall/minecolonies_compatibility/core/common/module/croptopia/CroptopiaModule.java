package steve_gall.minecolonies_compatibility.core.common.module.croptopia;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;

public class CroptopiaModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new LeafCropFruit());
	}

}
