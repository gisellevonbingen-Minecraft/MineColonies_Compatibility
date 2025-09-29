package steve_gall.minecolonies_compatibility.module.common.ae2;

import com.minecolonies.api.creativetab.ModCreativeTabs;

import appeng.blockentity.networking.CableBusBlockEntity;
import appeng.init.client.InitScreens;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
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

		var fml_bus = ModLoadingContext.get().getActiveContainer().getEventBus();
		ModuleItems.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);
		fml_bus.addListener(this::onBuildCreativeModeTabContents);

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
	protected void onRegisterMenuScreens(RegisterMenuScreensEvent e)
	{
		super.onRegisterMenuScreens(e);

		InitScreens.register(e, ModuleMenuTypes.CITIZEN_TERMINAL.get(), CitizenTerminalScreen::new, "/screens/minecolonies_compatibility/citizen_terminal.json");
	}

	private void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent e)
	{
		if (e.getTab() == ModCreativeTabs.GENERAL.get())
		{
			e.accept(ModuleParts.CITIZEN_TERMINAL.get());
		}

	}

}
