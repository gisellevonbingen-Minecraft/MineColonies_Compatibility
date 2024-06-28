package steve_gall.minecolonies_compatibility.module.common.storagenetwork.init;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.storagenetwork.CitizenInventoryBlock;

public class ModuleBlocks
{
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<CitizenInventoryBlock> CITIZEN_INVENTORY = REGISTER.register("citizen_inventory", CitizenInventoryBlock::new);

	private ModuleBlocks()
	{

	}

}
