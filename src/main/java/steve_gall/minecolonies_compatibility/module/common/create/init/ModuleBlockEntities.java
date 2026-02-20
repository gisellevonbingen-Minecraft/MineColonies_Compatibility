package steve_gall.minecolonies_compatibility.module.common.create.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.create.CitizenStockKeeperBlockEntity;

public class ModuleBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MineColoniesCompatibility.MOD_ID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CitizenStockKeeperBlockEntity>> CITIZEN_STOCK_KEEPER = REGISTER.register("citizen_stock_keeper", () -> BlockEntityType.Builder.of(CitizenStockKeeperBlockEntity::new, ModuleBlocks.CITIZEN_STOCK_KEEPER.get()).build(null));

	private ModuleBlockEntities()
	{

	}

}
