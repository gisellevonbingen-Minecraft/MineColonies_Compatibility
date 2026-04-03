package steve_gall.minecolonies_compatibility.mixin.common.cobblemon;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.cobblemon.mod.common.block.BerryBlock;

import net.minecraft.world.level.block.state.BlockState;

@Pseudo
@Mixin(value = BerryBlock.class, remap = false)
public interface BerryBlockAccessor
{
	@Invoker(value = "isMaxAge", remap = false)
	boolean invokeIsMaxAage(BlockState state);
}
