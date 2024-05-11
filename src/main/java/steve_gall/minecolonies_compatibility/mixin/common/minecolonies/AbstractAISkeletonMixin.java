package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.core.entity.ai.basic.AbstractAISkeleton;

import steve_gall.minecolonies_compatibility.core.common.entity.AbstractEntityAIBasicExtension;

@Mixin(value = AbstractAISkeleton.class, remap = false)
public abstract class AbstractAISkeletonMixin
{
	@Inject(method = "tick", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void tick(CallbackInfo ci)
	{
		if (this instanceof AbstractEntityAIBasicExtension extension)
		{
			extension.minecolonies_compatibility$onTick();
		}

	}

}
