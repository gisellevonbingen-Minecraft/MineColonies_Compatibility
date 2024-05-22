package steve_gall.minecolonies_compatibility.module.common.croptopia;

import com.epherical.croptopia.register.helpers.TreeCrop;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class CroptopiaModule
{
	public static void onLoad()
	{
		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(CroptopiaModule::onFMLCommonSetup);
	}

	private static void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		e.enqueueWork(() ->
		{
			for (var tree : TreeCrop.copy())
			{
				CustomizedFruit.register(new LeafCropFruit(tree));
			}

		});
	}

}
