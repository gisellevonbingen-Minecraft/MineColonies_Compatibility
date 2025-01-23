package steve_gall.minecolonies_compatibility.module.common.neapolitan;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class NeapolitanModule extends AbstractModule
{
	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedCrop.register(new StrawBerryCrop());
			CustomizedFruit.register(new AdzukiSproutsFruit());
			CustomizedFruit.register(new MintLeavesFruit());
			CustomizedFruit.register(new VanillaPodsFruit());
		});
	}

}
