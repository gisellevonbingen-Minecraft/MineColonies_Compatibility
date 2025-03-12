package steve_gall.minecolonies_compatibility.module.common.storagedrawers;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.storagedrawers.init.ModuleBlockEntities;
import steve_gall.minecolonies_compatibility.module.common.storagedrawers.init.ModuleBlocks;
import steve_gall.minecolonies_compatibility.module.common.storagedrawers.init.ModuleItems;

public class StorageDrawersModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleBlocks.REGISTER.register(fml_bus);
		ModuleItems.REGISTER.register(fml_bus);
		ModuleBlockEntities.REGISTER.register(fml_bus);
	}

}
