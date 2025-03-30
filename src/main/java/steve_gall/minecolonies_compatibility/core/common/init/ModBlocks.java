package steve_gall.minecolonies_compatibility.core.common.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.block.CommonNetworkStorageBlock;

public class ModBlocks
{
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<CommonNetworkStorageBlock> COMMON_NETWORK_STORAGE = REGISTER.register("common_network_storage", () -> new CommonNetworkStorageBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(1.0F)));

	private ModBlocks()
	{

	}

}
