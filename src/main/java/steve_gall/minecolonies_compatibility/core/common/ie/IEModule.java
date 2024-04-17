package steve_gall.minecolonies_compatibility.core.common.ie;

import blusunrize.immersiveengineering.api.tool.BulletHandler;
import blusunrize.immersiveengineering.common.items.RevolverItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.api.common.entity.CustomizedCitizenAISelectEvent;
import steve_gall.minecolonies_compatibility.core.common.AbstractModule;
import steve_gall.minecolonies_compatibility.core.common.init.ModJobs;
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

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();

		var forge_bus = MinecraftForge.EVENT_BUS;
		forge_bus.addListener(this::onCustomCombatAI);
	}

	private void onCustomCombatAI(CustomizedCitizenAISelectEvent e)
	{
		if (CitizenHelper.getJobEntry(e.getCitizen()) == ModJobs.GUNNER.get() && e.getWeapon().getItem() instanceof RevolverItem)
		{
			e.select(GunnerRevolverAI.INSTANCE);
		}

	}

}
