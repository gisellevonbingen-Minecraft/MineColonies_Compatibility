package steve_gall.minecolonies_compatibility.module.common.reliquary;

import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;

public class ReliquaryModule
{
	public static void onLoad()
	{
		CustomizedAI.register(new GunnerHandgunAI());
		DeliverableObjectRegistry.INSTANCE.register(Magazine.ID, Magazine::serialize, Magazine::deserialize);
	}

}
