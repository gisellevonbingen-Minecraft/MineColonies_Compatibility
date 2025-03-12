package steve_gall.minecolonies_compatibility.module.common.functionalstorage.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.functionalstorage.CitizenExtensionTile;

public class ModuleBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<BlockEntityType<CitizenExtensionTile>> CITIZEN_EXTENSION = REGISTER.register("citizen_extension", () -> BlockEntityType.Builder.of(ModuleBlocks.CITIZEN_EXTENSION.get().getTileEntityFactory(), ModuleBlocks.CITIZEN_EXTENSION.get()).build(null));

	private ModuleBlockEntities()
	{

	}

}
