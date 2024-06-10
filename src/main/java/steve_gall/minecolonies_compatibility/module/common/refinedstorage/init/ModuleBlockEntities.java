package steve_gall.minecolonies_compatibility.module.common.refinedstorage.init;

import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationManager;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationSpec;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.CitizenGridBlockEntity;

public class ModuleBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<BlockEntityType<CitizenGridBlockEntity>> CITIZEN_GRID = REGISTER.register("citizen_grid", () -> registerSynchronizationParameters(CitizenGridBlockEntity.SYNC_SPEC, BlockEntityType.Builder.of(CitizenGridBlockEntity::new, ModuleBlocks.CITIZEN_GRID.get()).build(null)));

	private static <T extends BlockEntity> BlockEntityType<T> registerSynchronizationParameters(BlockEntitySynchronizationSpec spec, BlockEntityType<T> t)
	{
		spec.getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);
		return t;
	}

	private ModuleBlockEntities()
	{

	}

}
