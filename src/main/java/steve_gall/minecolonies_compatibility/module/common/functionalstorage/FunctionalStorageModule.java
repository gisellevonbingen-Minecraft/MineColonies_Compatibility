package steve_gall.minecolonies_compatibility.module.common.functionalstorage;

import com.minecolonies.api.creativetab.ModCreativeTabs;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.functionalstorage.init.ModuleBlockEntities;
import steve_gall.minecolonies_compatibility.module.common.functionalstorage.init.ModuleBlocks;
import steve_gall.minecolonies_compatibility.module.common.functionalstorage.init.ModuleItems;

public class FunctionalStorageModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleBlocks.REGISTER.register(fml_bus);
		ModuleItems.REGISTER.register(fml_bus);
		ModuleBlockEntities.REGISTER.register(fml_bus);
		fml_bus.addListener(this::onBuildCreativeModeTabContents);
	}

	private void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent e)
	{
		if (e.getTab() == ModCreativeTabs.GENERAL.get())
		{
			e.accept(ModuleItems.CITIZEN_EXTENSION);
		}

	}

}
