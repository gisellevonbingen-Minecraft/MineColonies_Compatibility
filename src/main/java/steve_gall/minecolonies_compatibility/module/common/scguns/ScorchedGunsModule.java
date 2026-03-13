package steve_gall.minecolonies_compatibility.module.common.scguns;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;
import top.ribs.scguns.item.GunItem;

public class ScorchedGunsModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		DeliverableObjectRegistry.INSTANCE.register(Ammo.ID, Ammo::serialize, Ammo::deserialize);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);

		e.enqueueWork(() ->
		{
			CustomizedAI.register(new GunnerGunAI());
			ModToolTypes.GUN.register(item -> item instanceof GunItem);
		});

	}

}
