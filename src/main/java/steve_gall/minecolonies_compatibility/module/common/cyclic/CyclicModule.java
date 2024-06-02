package steve_gall.minecolonies_compatibility.module.common.cyclic;

import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;

import net.minecraft.world.item.Items;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class CyclicModule extends AbstractModule
{
	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedFruit.register(new AppleSproutFruit(BlockRegistry.APPLE_SPROUT, () -> Items.APPLE));
			CustomizedFruit.register(new AppleSproutFruit(BlockRegistry.APPLE_SPROUT_DIAMOND, ItemRegistry.APPLE_DIAMOND));
			CustomizedFruit.register(new AppleSproutFruit(BlockRegistry.APPLE_SPROUT_EMERALD, ItemRegistry.APPLE_EMERALD));
		});
	}

}
