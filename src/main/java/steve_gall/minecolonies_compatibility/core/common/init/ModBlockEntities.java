package steve_gall.minecolonies_compatibility.core.common.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.block.entity.CommonNetworkStorageBlockEntity;

public class ModBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<BlockEntityType<CommonNetworkStorageBlockEntity>> COMMON_NETWORK_STORAGE = REGISTER.register("common_network_storage", () -> BlockEntityType.Builder.of(CommonNetworkStorageBlockEntity::new, ModBlocks.COMMON_NETWORK_STORAGE.get()).build(null));

	private ModBlockEntities()
	{

	}

}
