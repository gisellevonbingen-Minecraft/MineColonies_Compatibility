package steve_gall.minecolonies_compatibility.module.common.ae2.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class ModuleItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MineColoniesCompatibility.MOD_ID);

	private ModuleItems()
	{

	}

}
