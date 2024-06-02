package steve_gall.minecolonies_compatibility.module.common.cobblemon;

import com.cobblemon.mod.common.CobblemonBlocks;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class CobblemonModule extends AbstractModule
{
	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			for (var entry : CobblemonBlocks.INSTANCE.berries().entrySet())
			{
				CustomizedFruit.register(new BerryFruit(entry.getValue()));
			}
		});
	}

}
