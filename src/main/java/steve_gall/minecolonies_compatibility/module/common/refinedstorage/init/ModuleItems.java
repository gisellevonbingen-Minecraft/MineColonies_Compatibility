package steve_gall.minecolonies_compatibility.module.common.refinedstorage.init;

import com.minecolonies.api.creativetab.ModCreativeTabs;
import com.refinedmods.refinedstorage.block.BaseBlock;
import com.refinedmods.refinedstorage.item.blockitem.BaseBlockItem;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class ModuleItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<BlockItem> CITIZEN_GRID = registerBlockItemFor(ModuleBlocks.CITIZEN_GRID);

	private static <T extends BaseBlock> RegistryObject<BlockItem> registerBlockItemFor(RegistryObject<T> block)
	{
		return REGISTER.register(block.getId().getPath(), () -> new BaseBlockItem(block.get(), new Item.Properties().tab(ModCreativeTabs.MINECOLONIES)));
	}

	private ModuleItems()
	{

	}

}
