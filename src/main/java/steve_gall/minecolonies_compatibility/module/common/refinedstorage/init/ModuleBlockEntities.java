package steve_gall.minecolonies_compatibility.module.common.refinedstorage.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.CitizenGridBlockEntity;

public class ModuleBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MineColoniesCompatibility.MOD_ID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CitizenGridBlockEntity>> CITIZEN_GRID = REGISTER.register("citizen_grid", () -> BlockEntityType.Builder.of(CitizenGridBlockEntity::new, ModuleBlocks.CITIZEN_GRID.get()).build(null));

	private ModuleBlockEntities()
	{

	}

}
