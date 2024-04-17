package steve_gall.minecolonies_compatibility.core.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class MineColoniesCompatibilityClient
{
	public MineColoniesCompatibilityClient()
	{
		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		var forge_bus = MinecraftForge.EVENT_BUS;
	}

}
