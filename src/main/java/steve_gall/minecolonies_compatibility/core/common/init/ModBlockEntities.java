package steve_gall.minecolonies_compatibility.core.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.block.entity.CommonNetworkStorageBlockEntity;

public class ModBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CommonNetworkStorageBlockEntity>> COMMON_NETWORK_STORAGE = REGISTER.register("common_network_storage", () -> BlockEntityType.Builder.of(CommonNetworkStorageBlockEntity::new, ModBlocks.COMMON_NETWORK_STORAGE.get()).build(null));

	private ModBlockEntities()
	{

	}

}
