package steve_gall.minecolonies_compatibility.mixin.common.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.projectile.AbstractArrow;

@Mixin(value = AbstractArrow.class, remap = true)
public interface AbstractArrowAccessor
{
	@Invoker(value = "setPierceLevel", remap = true)
	void invokeSetPierceLevel(byte level);
}
