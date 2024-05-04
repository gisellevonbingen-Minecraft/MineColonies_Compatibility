package steve_gall.minecolonies_compatibility.core.common.module.ie;

import blusunrize.immersiveengineering.api.tool.BulletHandler;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedCrop;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.RequestableObjectRegistry;

public class IEModule
{
	public static void onLoad()
	{
		RequestableObjectRegistry.register(Bullet.ID, Bullet::serialize, Bullet::deserialize);
		BulletHandler.registerBullet(DefaultBullet.ID, DefaultBullet.INSTANCE);
		CustomizedAI.register(new GunnerRevolverAI());
		CustomizedCrop.register(new HempCrop());
	}

}
