package steve_gall.minecolonies_compatibility.core.common.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.item.RestrictToolItem;

public class ModItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<RestrictToolItem> RESTRICT_TOOL = REGISTER.register("restrict_tool", () -> new RestrictToolItem(new Item.Properties()));
	public static final RegistryObject<BlockItem> COMMON_NETWORK_STORAGE = REGISTER.register("common_network_storage", () -> new BlockItem(ModBlocks.COMMON_NETWORK_STORAGE.get(), new Item.Properties()));

	private ModItems()
	{

	}

}
