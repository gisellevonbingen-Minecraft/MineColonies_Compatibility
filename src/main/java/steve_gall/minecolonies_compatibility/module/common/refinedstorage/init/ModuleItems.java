package steve_gall.minecolonies_compatibility.module.common.refinedstorage.init;

import com.refinedmods.refinedstorage.common.support.BaseBlockItem;
import com.refinedmods.refinedstorage.common.support.BlockItemProvider;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class ModuleItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(BuiltInRegistries.ITEM, MineColoniesCompatibility.MOD_ID);

	public static final DeferredHolder<Item, BaseBlockItem> CITIZEN_GRID = registerBlockItemFor(ModuleBlocks.CITIZEN_GRID);

	private static <BLOCK extends Block & BlockItemProvider<ITEM>, ITEM extends BlockItem> DeferredHolder<Item, ITEM> registerBlockItemFor(DeferredHolder<Block, BLOCK> block)
	{
		return REGISTER.register(block.getId().getPath(), () -> block.get().createBlockItem());
	}

	private ModuleItems()
	{

	}

}
