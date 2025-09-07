package steve_gall.minecolonies_compatibility.mixin.common.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StemBlock;

@Mixin(value = StemBlock.class, remap = true)
public interface StemBlockAccessor
{
	@Accessor(value = "fruit", remap = true)
	ResourceKey<Block> getFruit();
}
