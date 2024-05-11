package steve_gall.minecolonies_compatibility.module.common.ars_nouveau;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class ArsNouveauModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new SourceBerryFruit());
	}

}
