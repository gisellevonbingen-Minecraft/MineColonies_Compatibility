package steve_gall.minecolonies_compatibility.mixin.common.blue_skies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.legacy.blue_skies.blocks.natural.BrewberryBushBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = BrewberryBushBlock.class, remap = false)
public interface BrewberryBushBlockAccessor
{
	@Invoker(value = "getBerry", remap = false)
	ItemStack invokeGetBerry(Level level, BlockState state, BlockPos pos);
}
