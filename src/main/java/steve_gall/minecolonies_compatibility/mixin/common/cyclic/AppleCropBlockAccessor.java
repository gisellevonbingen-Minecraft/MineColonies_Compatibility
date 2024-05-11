package steve_gall.minecolonies_compatibility.mixin.common.cyclic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.lothrazar.cyclic.block.apple.AppleCropBlock;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

@Mixin(value = AppleCropBlock.class, remap = false)
public interface AppleCropBlockAccessor
{
	@Accessor(value = "MAX_AGE", remap = false)
	static int getMaxAge()
	{
		throw new AssertionError();
	}

	@Accessor(value = "AGE", remap = false)
	static IntegerProperty getAge()
	{
		throw new AssertionError();
	}

}
