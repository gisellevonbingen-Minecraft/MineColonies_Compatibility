package steve_gall.minecolonies_compatibility.module.common.functionalstorage.init;

import java.util.function.Function;

import com.hrznstudio.titanium.block.BasicBlock;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.functionalstorage.CitizenExtensionBlock;

public class ModuleBlocks
{
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<CitizenExtensionBlock> CITIZEN_EXTENSION = register("citizen_extension", CitizenExtensionBlock::new);

	public static <BLOCK extends BasicBlock> RegistryObject<BLOCK> register(String name, Function<String, BLOCK> function)
	{
		return REGISTER.register(name, () -> function.apply(name));
	}

	private ModuleBlocks()
	{

	}

}
