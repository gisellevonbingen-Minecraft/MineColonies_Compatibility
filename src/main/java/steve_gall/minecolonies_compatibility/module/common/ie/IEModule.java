package steve_gall.minecolonies_compatibility.module.common.ie;

import blusunrize.immersiveengineering.api.tool.BulletHandler;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;

public class IEModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		DeliverableObjectRegistry.INSTANCE.register(Bullet.ID, Bullet::serialize, Bullet::deserialize);
		BulletHandler.registerBullet(DefaultBullet.ID, DefaultBullet.INSTANCE);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedAI.register(new GunnerRevolverAI());
			CustomizedCrop.register(new HempCrop());
		});
	}

}
