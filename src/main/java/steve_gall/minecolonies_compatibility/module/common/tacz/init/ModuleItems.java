package steve_gall.minecolonies_compatibility.module.common.tacz.init;

import com.minecolonies.api.creativetab.ModCreativeTabs;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.tacz.item.DummyGunItem;

public class ModuleItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<DummyGunItem> DUMMY_GUN = REGISTER.register("tacz_dummy_gun", () -> new DummyGunItem(new Item.Properties().tab(ModCreativeTabs.MINECOLONIES)));

	private ModuleItems()
	{

	}

}
