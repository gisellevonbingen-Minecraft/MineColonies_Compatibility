package steve_gall.minecolonies_compatibility.module.common.storagenetwork.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.storagenetwork.CitizenInventoryBlockEntity;

public class ModuleBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<BlockEntityType<CitizenInventoryBlockEntity>> CITIZEN_INVENTORY = REGISTER.register("citizen_inventory", () -> BlockEntityType.Builder.of(CitizenInventoryBlockEntity::new, ModuleBlocks.CITIZEN_INVENTORY.get()).build(null));

	private ModuleBlockEntities()
	{

	}

}
