package steve_gall.minecolonies_compatibility.module.common.cyclic;

import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;

import net.minecraft.world.item.Items;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class CyclicModule
{
	public static void onLoad()
	{
		CustomizedFruit.register(new AppleSproutFruit(BlockRegistry.APPLE_SPROUT::get, () -> Items.APPLE));
		CustomizedFruit.register(new AppleSproutFruit(BlockRegistry.APPLE_SPROUT_DIAMOND::get, ItemRegistry.APPLE_DIAMOND));
		CustomizedFruit.register(new AppleSproutFruit(BlockRegistry.APPLE_SPROUT_EMERALD::get, ItemRegistry.APPLE_EMERALD));
	}

}
