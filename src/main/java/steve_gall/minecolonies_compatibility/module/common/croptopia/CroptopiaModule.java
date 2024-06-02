package steve_gall.minecolonies_compatibility.module.common.croptopia;

import com.epherical.croptopia.register.helpers.TreeCrop;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class CroptopiaModule extends AbstractModule
{
	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			for (var tree : TreeCrop.copy())
			{
				CustomizedFruit.register(new LeafCropFruit(tree));
			}

		});
	}

}
