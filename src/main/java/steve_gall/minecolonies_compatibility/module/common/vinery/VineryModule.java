package steve_gall.minecolonies_compatibility.module.common.vinery;

import net.minecraft.world.item.BlockItem;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import satisfyu.vinery.registry.GrapeTypes;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class VineryModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new AppleLeavesFruit());
		CustomizedFruit.register(new CherryLeavesFruit());

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(VineryModule::onFMLCommonSetup);
	}

	private static void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		e.enqueueWork(() ->
		{
			for (var grapeType : GrapeTypes.GRAPE_TYPE_TYPES)
			{
				if (grapeType.getSeeds() instanceof BlockItem item)
				{
					CustomizedFruit.register(new GrapeFruit(grapeType, item.getBlock()));
				}

			}

		});
	}

}
