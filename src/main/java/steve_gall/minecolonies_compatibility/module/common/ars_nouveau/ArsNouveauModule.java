package steve_gall.minecolonies_compatibility.module.common.ars_nouveau;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class ArsNouveauModule extends AbstractModule
{
	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedFruit.register(new SourceBerryFruit());
		});
	}

}
