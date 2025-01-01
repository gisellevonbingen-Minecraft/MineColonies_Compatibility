package steve_gall.minecolonies_compatibility.module.common.storagenetwork;

import com.minecolonies.api.creativetab.ModCreativeTabs;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.building.module.NetworkStorageViewRegistry;
import steve_gall.minecolonies_compatibility.module.client.storagenetwork.CitizenInventoryScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.storagenetwork.init.ModuleBlockEntities;
import steve_gall.minecolonies_compatibility.module.common.storagenetwork.init.ModuleBlocks;
import steve_gall.minecolonies_compatibility.module.common.storagenetwork.init.ModuleItems;
import steve_gall.minecolonies_compatibility.module.common.storagenetwork.init.ModuleMenuTypes;

public class StorageNetworkModule extends AbstractModule
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
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			NetworkStorageViewRegistry.register((be, direction) -> be instanceof CitizenInventoryBlockEntity inventory ? inventory.getView() : null);
		});
	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);

		MenuScreens.register(ModuleMenuTypes.CITIZEN_INVENTORY.get(), CitizenInventoryScreen::new);
	}

	private void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent e)
	{
		if (e.getTab() == ModCreativeTabs.GENERAL.get())
		{
			e.accept(ModuleItems.CITIZEN_INVENTORY);
		}

	}

}
