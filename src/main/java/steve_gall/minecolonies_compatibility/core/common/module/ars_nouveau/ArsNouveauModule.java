package steve_gall.minecolonies_compatibility.core.common.module.ars_nouveau;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.core.common.module.AbstractModule;

public class ArsNouveauModule extends AbstractModule
{
	@Override
	public String getModId()
	{
		return "ars_nouveau";
	}

	@Override
	protected void onLoad()
	{
		CustomizedFruit.register(new SourceBerryFruit());
	}

}
