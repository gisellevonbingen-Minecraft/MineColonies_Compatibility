package steve_gall.minecolonies_compatibility.core.common.module.ie;

import blusunrize.immersiveengineering.api.tool.BulletHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.core.common.module.AbstractModule;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.RequestableObjectRegistry;

public class IEModule extends AbstractModule
{
	@Override
	public String getModId()
	{
		return "immersiveengineering";
	}

	@Override
	protected void onLoad()
	{
		RequestableObjectRegistry.register(Bullet.ID, Bullet::serialize, Bullet::deserialize);
		BulletHandler.registerBullet(DefaultBullet.ID, DefaultBullet.INSTANCE);
		CustomizedAI.register(new GunnerRevolverAI());

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		var forge_bus = MinecraftForge.EVENT_BUS;
	}

}
