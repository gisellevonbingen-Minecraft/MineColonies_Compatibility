package steve_gall.minecolonies_compatibility.module.common.create;

import com.minecolonies.api.creativetab.ModCreativeTabs;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.client.create.CitizenStockKeeperScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.create.init.ModuleBlockEntities;
import steve_gall.minecolonies_compatibility.module.common.create.init.ModuleBlocks;
import steve_gall.minecolonies_compatibility.module.common.create.init.ModuleItems;
import steve_gall.minecolonies_compatibility.module.common.create.init.ModuleMenuTypes;

public class CreateModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleBlocks.REGISTER.register(fml_bus);
		ModuleItems.REGISTER.register(fml_bus);
		ModuleBlockEntities.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);
		fml_bus.addListener(this::onBuildCreativeModeTabContents);

		var network = MineColoniesCompatibility.network();
		network.registerMessage(AddressMessage.class, AddressMessage::new);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);

		MenuScreens.register(ModuleMenuTypes.CITIZEN_STOCK_KEEPER.get(), CitizenStockKeeperScreen::new);
	}

	private void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent e)
	{
		if (e.getTab() == ModCreativeTabs.GENERAL.get())
		{
			e.accept(ModuleItems.CITIZEN_STOCK_KEEPER);
		}

	}

}
