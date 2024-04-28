package steve_gall.minecolonies_compatibility.core.common.module.minecraft;

import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.core.common.module.AbstractModule;

public class MinecraftModule extends AbstractModule
{
	@Override
	public String getModId()
	{
		return "minecraft";
	}

	@Override
	protected boolean canLoad()
	{
		return true;
	}

	@Override
	protected void onLoad()
	{
		CustomizedFruit.register(new SweetBerryFruit());
	}

}
