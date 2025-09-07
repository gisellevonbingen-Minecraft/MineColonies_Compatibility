package steve_gall.minecolonies_compatibility.core.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.item.RestrictToolItem;

public class ModItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(Registries.ITEM, MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<Item, Item> BUTCHERABLE_ICON = REGISTER.register("butcherable_icon", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, RestrictToolItem> RESTRICT_TOOL = REGISTER.register("restrict_tool", () -> new RestrictToolItem(new Item.Properties()));
	public static final DeferredHolder<Item, BlockItem> COMMON_NETWORK_STORAGE = REGISTER.register("common_network_storage", () -> new BlockItem(ModBlocks.COMMON_NETWORK_STORAGE.get(), new Item.Properties()));

	private ModItems()
	{

	}

}
