package steve_gall.minecolonies_compatibility.core.common.module.pamhc2trees;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.core.common.module.AbstractModule;

public class PamsHarvestCraft2TreesModule extends AbstractModule
{
	@Override
	public String getModId()
	{
		return "pamhc2trees";
	}

	@Override
	protected void onLoad()
	{
		CustomizedFruit.register(new PamFruit());
	}

}
