package steve_gall.minecolonies_compatibility.core.common.module.thermal;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.core.common.module.AbstractModule;

public class ThermalModule extends AbstractModule
{
	@Override
	public String getModId()
	{
		return "thermal";
	}

	@Override
	protected void onLoad()
	{
		CustomizedCrop.register(new PerennialCrop());
	}

}
