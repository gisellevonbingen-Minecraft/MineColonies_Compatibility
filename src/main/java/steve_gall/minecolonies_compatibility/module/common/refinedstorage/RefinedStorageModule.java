package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import com.minecolonies.api.creativetab.ModCreativeTabs;
import com.refinedmods.refinedstorage.apiimpl.API;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.building.module.NetworkStorageViewRegistry;
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
			NetworkStorageViewRegistry.register((be, direction) -> be instanceof CitizenGridBlockEntity grid ? grid.getNode().getView() : null);
			API.instance().getNetworkNodeRegistry().add(CitizenGridNetworkNode.ID, CitizenGridNetworkNode::new);
		});
	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);

		MenuScreens.register(ModuleMenuTypes.CITIZEN_GRID.get(), CitizenGridScreen::new);
	}

	private void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent e)
	{
		if (e.getTab() == ModCreativeTabs.GENERAL.get())
		{
			e.accept(ModuleItems.CITIZEN_GRID);
		}

	}

}
