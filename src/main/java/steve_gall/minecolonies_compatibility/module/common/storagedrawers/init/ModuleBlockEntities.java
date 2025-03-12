package steve_gall.minecolonies_compatibility.module.common.storagedrawers.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.storagedrawers.CitizenSlaveTile;

public class ModuleBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<BlockEntityType<CitizenSlaveTile>> CITIZEN_SLAVE = REGISTER.register("citizen_slave", () -> BlockEntityType.Builder.of(CitizenSlaveTile::new, ModuleBlocks.CITIZEN_SLAVE.get()).build(null));

	private ModuleBlockEntities()
	{

	}

}
