package steve_gall.minecolonies_compatibility.core.common.module.farmersdelight;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.core.common.module.AbstractModule;

public class FarmersDelightModule extends AbstractModule
{
	@Override
	public String getModId()
	{
		return "farmersdelight";
	}

	@Override
	protected void onLoad()
	{
		CustomizedCrop.register(new TomatoCrop());
	}

}
