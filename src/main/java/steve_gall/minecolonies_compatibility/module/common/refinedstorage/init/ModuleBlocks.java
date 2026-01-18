package steve_gall.minecolonies_compatibility.module.common.refinedstorage.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.CitizenGridBlock;

public class ModuleBlocks
{
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, MineColoniesCompatibility.MOD_ID);

	public static final DeferredHolder<Block, CitizenGridBlock> CITIZEN_GRID = REGISTER.register("citizen_grid", CitizenGridBlock::new);

	private ModuleBlocks()
	{

	}

}
