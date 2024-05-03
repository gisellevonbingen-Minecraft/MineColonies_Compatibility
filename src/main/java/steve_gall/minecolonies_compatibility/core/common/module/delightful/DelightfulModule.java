package steve_gall.minecolonies_compatibility.core.common.module.delightful;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.core.common.module.AbstractModule;

public class DelightfulModule extends AbstractModule
{
	@Override
	public String getModId()
	{
		return "delightful";
	}

	@Override
	protected void onLoad()
	{
		CustomizedCrop.register(new SalmonberryCrop());
		CustomizedFruit.register(new SalmonberryFruit());
	}

}
