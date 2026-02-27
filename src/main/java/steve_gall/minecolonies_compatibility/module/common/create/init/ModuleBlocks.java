package steve_gall.minecolonies_compatibility.module.common.create.init;

import com.simibubi.create.foundation.data.SharedProperties;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.create.CitizenStockKeeperBlock;

public class ModuleBlocks
{
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<CitizenStockKeeperBlock> CITIZEN_STOCK_KEEPER = REGISTER.register("citizen_stock_keeper", () -> new CitizenStockKeeperBlock(BlockBehaviour.Properties.copy(SharedProperties.softMetal())));

	private ModuleBlocks()
	{

	}

}
