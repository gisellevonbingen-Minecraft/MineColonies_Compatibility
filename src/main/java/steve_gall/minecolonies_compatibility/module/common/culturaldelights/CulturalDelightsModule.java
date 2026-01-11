package steve_gall.minecolonies_compatibility.module.common.culturaldelights;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class CulturalDelightsModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedCrop.register(new CornCrop());
			CustomizedFruit.register(new AvocadoFruit());
		});
	}

}
