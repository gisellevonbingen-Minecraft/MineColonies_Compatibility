package steve_gall.minecolonies_compatibility.module.common.cobblemon;

import com.cobblemon.mod.common.CobblemonBlocks;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class CobblemonModule
{
	public static void onLoad()
	{
		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(CobblemonModule::onFMLCommonSetup);
	}

	private static void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		e.enqueueWork(() ->
		{
			for (var entry : CobblemonBlocks.INSTANCE.berries().entrySet())
			{
				CustomizedFruit.register(new BerryFruit(entry.getValue()));
			}
		});
	}

}
