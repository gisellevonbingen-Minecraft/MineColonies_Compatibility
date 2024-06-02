package steve_gall.minecolonies_compatibility.module.common.regions_unexplored;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class RegionsUnexploredModule
{
	public static void onLoad()
	{
		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(RegionsUnexploredModule::onFMLCommonSetup);
	}

	private static void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		e.enqueueWork(() ->
		{
			CustomizedFruit.register(new AppleLeavesFruit());
			CustomizedFruit.register(new SalmonBerryFruit());
		});
	}

}
