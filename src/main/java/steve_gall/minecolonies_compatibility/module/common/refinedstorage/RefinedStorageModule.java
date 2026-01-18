package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import com.minecolonies.api.creativetab.ModCreativeTabs;
import com.refinedmods.refinedstorage.neoforge.api.RefinedStorageNeoForgeApi;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import steve_gall.minecolonies_compatibility.module.client.refinedstorage.CitizenGridScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.init.ModuleBlockEntities;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.init.ModuleBlocks;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.init.ModuleItems;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.init.ModuleMenuTypes;

public class RefinedStorageModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = ModLoadingContext.get().getActiveContainer().getEventBus();
		ModuleBlocks.REGISTER.register(fml_bus);
		ModuleItems.REGISTER.register(fml_bus);
		ModuleBlockEntities.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);
		fml_bus.addListener(this::onRegisterCapabilities);
		fml_bus.addListener(this::onBuildCreativeModeTabContents);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
	}

	@Override
	protected void onRegisterMenuScreens(RegisterMenuScreensEvent e)
	{
		super.onRegisterMenuScreens(e);

		e.register(ModuleMenuTypes.CITIZEN_GRID.get(), CitizenGridScreen::new);
	}

	private void onRegisterCapabilities(RegisterCapabilitiesEvent event)
	{
		event.registerBlockEntity(RefinedStorageNeoForgeApi.INSTANCE.getNetworkNodeContainerProviderCapability(), ModuleBlockEntities.CITIZEN_GRID.get(), (be, side) -> be.getContainerProvider());
	}

	private void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent e)
	{
		if (e.getTab() == ModCreativeTabs.GENERAL.get())
		{
			e.accept(ModuleItems.CITIZEN_GRID.get());
		}

	}

}
