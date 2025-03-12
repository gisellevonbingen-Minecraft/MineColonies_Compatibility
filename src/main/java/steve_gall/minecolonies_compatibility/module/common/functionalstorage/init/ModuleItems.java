package steve_gall.minecolonies_compatibility.module.common.functionalstorage.init;

import com.minecolonies.api.creativetab.ModCreativeTabs;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class ModuleItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<BlockItem> CITIZEN_EXTENSION = REGISTER.register("citizen_extension", () -> new BlockItem(ModuleBlocks.CITIZEN_EXTENSION.get(), new Item.Properties().tab(ModCreativeTabs.MINECOLONIES)));

	private ModuleItems()
	{

	}

}
