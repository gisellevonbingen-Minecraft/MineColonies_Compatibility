package steve_gall.minecolonies_compatibility.core.common.module.delightful;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;

public class DelightfulModule
{
	public static void onLoad()
	{
		CustomizedCrop.register(new SalmonberryCrop());
		CustomizedFruit.register(new SalmonberryFruit());
	}

}
