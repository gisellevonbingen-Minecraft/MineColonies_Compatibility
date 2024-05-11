package steve_gall.minecolonies_compatibility.module.common.thermal;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;

public class ThermalModule
{
	public static void onLoad()
	{
		CustomizedCrop.register(new PerennialCrop());
	}

}
