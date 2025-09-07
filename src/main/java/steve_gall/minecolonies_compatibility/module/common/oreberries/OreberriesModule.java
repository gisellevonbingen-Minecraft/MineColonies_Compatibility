package steve_gall.minecolonies_compatibility.module.common.oreberries;

import com.mrbysco.oreberriesreplanted.block.OreBerryBushBlock;
import com.mrbysco.oreberriesreplanted.registry.OreBerryRegistry;

import net.minecraft.world.level.block.Block;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class OreberriesModule extends AbstractModule
{
	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			for (DeferredHolder<Block, ? extends Block> registryObject : OreBerryRegistry.BLOCKS.getEntries())
			{
				if (registryObject.get() instanceof OreBerryBushBlock block)
				{
					CustomizedFruit.register(new OreBerryFruit(block));
				}

			}

		});
	}

}
