package steve_gall.minecolonies_compatibility.module.common.vinery;

import net.minecraft.world.item.BlockItem;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import satisfyu.vinery.registry.GrapeTypeRegistry;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class VineryModule extends AbstractModule
{
	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedFruit.register(new AppleLeavesFruit());
			CustomizedFruit.register(new CherryLeavesFruit());

			for (var grapeType : GrapeTypeRegistry.GRAPE_TYPE_TYPES)
			{
				if (grapeType.getSeeds() instanceof BlockItem item)
				{
					CustomizedFruit.register(new GrapeFruit(grapeType, item.getBlock()));
				}

			}

		});
	}

}
