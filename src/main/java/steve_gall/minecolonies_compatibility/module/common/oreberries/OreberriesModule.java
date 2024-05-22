package steve_gall.minecolonies_compatibility.module.common.oreberries;

import com.mrbysco.oreberriesreplanted.block.OreBerryBushBlock;
import com.mrbysco.oreberriesreplanted.registry.OreBerryRegistry;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class OreberriesModule
{
	public static void onLoad()
	{
		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(OreberriesModule::onFMLCommonSetup);
	}

	private static void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		e.enqueueWork(() ->
		{
			for (RegistryObject<Block> registryObject : OreBerryRegistry.BLOCKS.getEntries())
			{
				if (registryObject.get() instanceof OreBerryBushBlock block)
				{
					CustomizedFruit.register(new OreBerryFruit(block));
				}

			}

		});
	}

}
