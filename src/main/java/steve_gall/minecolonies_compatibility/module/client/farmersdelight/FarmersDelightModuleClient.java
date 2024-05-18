package steve_gall.minecolonies_compatibility.module.client.farmersdelight;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleMenuTypes;

public class FarmersDelightModuleClient
{
	public static void onLoad()
	{
		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(FarmersDelightModuleClient::onFMLClientSetup);
	}

	private static void onFMLClientSetup(FMLClientSetupEvent event)
	{
		MenuScreens.register(ModuleMenuTypes.TEACH_CUTTING.get(), TeachCuttingScreen::new);
		MenuScreens.register(ModuleMenuTypes.TEACH_COOKING.get(), TeachCookingScreen::new);
	}

}
