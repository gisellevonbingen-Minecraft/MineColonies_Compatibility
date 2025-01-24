package steve_gall.minecolonies_compatibility.module.common.nethersdelight;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import umpaz.nethersdelight.common.block.FungusColonyBlock;

public class NethersDelightModule extends AbstractModule
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
			CustomizedFruit.register(new PropelpPearlFruit());

			for (var block : ForgeRegistries.BLOCKS.getValues())
			{
				if (block instanceof FungusColonyBlock fungusColony)
				{
					CustomizedFruit.register(new FungusColonyFruit(fungusColony));
				}

			}

		});
	}

}
