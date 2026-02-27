package steve_gall.minecolonies_compatibility.module.common.create.init;

import com.simibubi.create.foundation.data.SharedProperties;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.create.CitizenStockKeeperBlock;

public class ModuleBlocks
{
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, MineColoniesCompatibility.MOD_ID);

	public static final DeferredHolder<Block, CitizenStockKeeperBlock> CITIZEN_STOCK_KEEPER = REGISTER.register("citizen_stock_keeper", () -> new CitizenStockKeeperBlock(BlockBehaviour.Properties.ofFullCopy(SharedProperties.softMetal())));

	private ModuleBlocks()
	{

	}

}
