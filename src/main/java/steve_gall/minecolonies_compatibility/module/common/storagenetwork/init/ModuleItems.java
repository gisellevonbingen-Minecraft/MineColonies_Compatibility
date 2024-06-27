package steve_gall.minecolonies_compatibility.module.common.storagenetwork.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class ModuleItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<BlockItem> CITIZEN_INVENTORY = REGISTER.register("citizen_inventory", () -> new BlockItem(ModuleBlocks.CITIZEN_INVENTORY.get(), new Item.Properties()));

	private ModuleItems()
	{

	}

}
