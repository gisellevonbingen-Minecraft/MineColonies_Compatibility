package steve_gall.minecolonies_compatibility.core.common.module.croptopia;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.core.common.module.AbstractModule;

public class CroptopiaModule extends AbstractModule
{
	@Override
	public String getModId()
	{
		return "croptopia";
	}

	@Override
	protected void onLoad()
	{
		CustomizedFruit.register(new LeafCropFruit());
	}

}
