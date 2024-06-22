package steve_gall.minecolonies_compatibility.module.common.ae2;

import appeng.blockentity.networking.CableBusBlockEntity;
import appeng.init.client.InitScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.building.module.NetworkStorageViewRegistry;
import steve_gall.minecolonies_compatibility.module.client.ae2.CitizenTerminalScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.ae2.init.ModuleItems;
import steve_gall.minecolonies_compatibility.module.common.ae2.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.ae2.init.ModuleParts;

public class AppliedEnergistics2Module extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleItems.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);

		ModuleParts.init();
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			NetworkStorageViewRegistry.register((be, direction) ->
			{
				if (be instanceof CableBusBlockEntity cable)
				{
					if (cable.getPart(direction) instanceof CitizenTerminalPart part)
					{
						return part.getView();
					}

				}

				return null;
			});
		});
	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);
		e.enqueueWork(() ->
		{
			InitScreens.register(ModuleMenuTypes.CITIZEN_TERMINAL.get(), CitizenTerminalScreen::new, "/screens/minecolonies_compatibility/citizen_terminal.json");
		});
	}

}
