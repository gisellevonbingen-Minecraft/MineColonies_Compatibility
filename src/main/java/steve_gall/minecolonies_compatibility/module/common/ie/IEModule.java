package steve_gall.minecolonies_compatibility.module.common.ie;

import blusunrize.immersiveengineering.api.tool.BulletHandler;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;

public class IEModule
{
	public static void onLoad()
	{
		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(IEModule::onFMLCommonSetup);

		CustomizedAI.register(new GunnerRevolverAI());
		CustomizedCrop.register(new HempCrop());
	}

	private static void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		DeliverableObjectRegistry.INSTANCE.register(Bullet.ID, Bullet::serialize, Bullet::deserialize);
		BulletHandler.registerBullet(DefaultBullet.ID, DefaultBullet.INSTANCE);
	}

}
