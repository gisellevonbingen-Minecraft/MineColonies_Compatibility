package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.core.entity.ai.workers.guard.AbstractEntityAIFight;

import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAIAttack;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.ICustomizableEntityAI;

@Mixin(value = AbstractEntityAIFight.class, remap = false)
public abstract class AbstractEntityAIFightMixin
{
	@Inject(method = "atBuildingActions", remap = false, at = @At(value = "TAIL"), cancellable = true)
	private void atBuildingActions(CallbackInfo ci)
	{
		if (this instanceof ICustomizableEntityAI self && self.getSelectedAI() instanceof CustomizedAIAttack attack)
		{
			attack.atBuildingActions(self.getAIContext());
		}

	}

}
