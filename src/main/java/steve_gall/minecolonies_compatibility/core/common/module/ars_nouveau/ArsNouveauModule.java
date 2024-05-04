package steve_gall.minecolonies_compatibility.core.common.module.ars_nouveau;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;

public class ArsNouveauModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new SourceBerryFruit());
	}

}
