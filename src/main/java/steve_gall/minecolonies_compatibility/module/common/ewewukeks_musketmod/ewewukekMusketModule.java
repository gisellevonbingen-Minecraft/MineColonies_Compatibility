package steve_gall.minecolonies_compatibility.module.common.ewewukeks_musketmod;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;

public class ewewukekMusketModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleItems.REGISTER.register(fml_bus);
		DeliverableObjectRegistry.INSTANCE.register(Cartridge.ID, Cartridge::serialize, Cartridge::deserialize);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedAI.register(new GunnerGunAI.Musket());
			CustomizedAI.register(new GunnerGunAI.Pistol());
			CustomizedAI.register(new GunnerGunAI.Blunderbuss());
		});
	}

}
