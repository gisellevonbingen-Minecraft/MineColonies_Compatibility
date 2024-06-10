package steve_gall.minecolonies_compatibility.module.common.refinedstorage.init;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.CitizenGridBlock;

public class ModuleBlocks
{
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<CitizenGridBlock> CITIZEN_GRID = REGISTER.register("citizen_grid", CitizenGridBlock::new);

	private ModuleBlocks()
	{

	}

}
