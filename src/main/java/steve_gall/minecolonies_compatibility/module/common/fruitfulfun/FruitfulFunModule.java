package steve_gall.minecolonies_compatibility.module.common.fruitfulfun;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import snownee.fruits.block.FruitLeavesBlock;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class FruitfulFunModule extends AbstractModule
{
	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			for (var block : ForgeRegistries.BLOCKS.getValues())
			{
				if (block instanceof FruitLeavesBlock fruitBlock)
				{
					CustomizedFruit.register(new FruitfulFunFruit(fruitBlock));
				}

			}

		});
	}

}
