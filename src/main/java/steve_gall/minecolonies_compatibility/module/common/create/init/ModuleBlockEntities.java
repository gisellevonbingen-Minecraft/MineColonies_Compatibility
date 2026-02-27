package steve_gall.minecolonies_compatibility.module.common.create.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.create.CitizenStockKeeperBlockEntity;

public class ModuleBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<BlockEntityType<CitizenStockKeeperBlockEntity>> CITIZEN_STOCK_KEEPER = REGISTER.register("citizen_stock_keeper", () -> BlockEntityType.Builder.of(CitizenStockKeeperBlockEntity::new, ModuleBlocks.CITIZEN_STOCK_KEEPER.get()).build(null));

	private ModuleBlockEntities()
	{

	}

}
