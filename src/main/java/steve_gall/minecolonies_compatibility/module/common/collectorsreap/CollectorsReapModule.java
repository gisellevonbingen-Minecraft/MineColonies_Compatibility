package steve_gall.minecolonies_compatibility.module.common.collectorsreap;

import net.brdle.collectorsreap.common.block.FruitBushBlock;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class CollectorsReapModule extends AbstractModule
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
			for (var block : ForgeRegistries.BLOCKS.getValues())
			{
				if (block instanceof FruitBushBlock fruitBushBlock)
				{
					CustomizedFruit.register(new FruitBushFruit(fruitBushBlock));
				}

			}
		});
	}

}
