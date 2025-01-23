package steve_gall.minecolonies_compatibility.module.common.atmospheric;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class AtmosphericModule extends AbstractModule
{
	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedFruit.register(new AloeVeraFruit());
			CustomizedFruit.register(new AloeVeraTallFruit());
			CustomizedFruit.register(new BarrelCactusFruit());
			CustomizedFruit.register(new DragonFruit());
			CustomizedFruit.register(new PassionFruit());
			CustomizedFruit.register(new YuccaFruit());
		});
	}

}
