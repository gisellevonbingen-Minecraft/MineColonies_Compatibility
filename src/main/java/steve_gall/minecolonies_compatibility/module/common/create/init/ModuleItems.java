package steve_gall.minecolonies_compatibility.module.common.create.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class ModuleItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(BuiltInRegistries.ITEM, MineColoniesCompatibility.MOD_ID);

	public static final DeferredHolder<Item, BlockItem> CITIZEN_STOCK_KEEPER = REGISTER.register("citizen_stock_keeper", () -> new BlockItem(ModuleBlocks.CITIZEN_STOCK_KEEPER.get(), new Item.Properties()));

	private ModuleItems()
	{

	}

}
