package steve_gall.minecolonies_compatibility.module.common.reliquary;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;

public class ReliquaryModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		DeliverableObjectRegistry.INSTANCE.register(Magazine.ID, Magazine::serialize, Magazine::deserialize);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedAI.register(new GunnerHandgunAI());
		});
	}

}
