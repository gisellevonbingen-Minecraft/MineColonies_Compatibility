package steve_gall.minecolonies_compatibility.module.common.create;

import com.minecolonies.api.creativetab.ModCreativeTabs;
import com.minecolonies.api.util.IItemHandlerCapProvider;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import steve_gall.minecolonies_compatibility.module.client.create.CitizenStockKeeperScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.create.init.ModuleBlockEntities;
import steve_gall.minecolonies_compatibility.module.common.create.init.ModuleBlocks;
import steve_gall.minecolonies_compatibility.module.common.create.init.ModuleItems;
import steve_gall.minecolonies_compatibility.module.common.create.init.ModuleMenuTypes;
import steve_gall.minecolonies_tweaks.api.common.network.MessageRegistrar;

public class CreateModule extends AbstractModule
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

		e.register(ModuleMenuTypes.CITIZEN_STOCK_KEEPER.get(), CitizenStockKeeperScreen::new);
	}

	@Override
	protected void onRegisterNetwork(MessageRegistrar channel)
	{
		super.onRegisterNetwork(channel);

		channel.playToServer(AddressMessage.TYPE, AddressMessage::new);
	}

	private void onRegisterCapabilities(RegisterCapabilitiesEvent e)
	{
		e.registerBlockEntity(ItemHandler.BLOCK, ModuleBlockEntities.CITIZEN_STOCK_KEEPER.get(), IItemHandlerCapProvider::getItemHandlerCap);
	}

	private void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent e)
	{
		if (e.getTab() == ModCreativeTabs.GENERAL.get())
		{
			e.accept(ModuleItems.CITIZEN_STOCK_KEEPER.get());
		}

	}

}
