package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.core.entity.ai.combat.TargetAI;

import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAIAttack;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.ICustomizableAttackMoveAI;

@Mixin(value = TargetAI.class, remap = false)
public abstract class TargetAIMixin
{
	@Inject(method = "getSearchRange", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void getSearchRange(CallbackInfoReturnable<Integer> cir)
	{
		if (this instanceof ICustomizableAttackMoveAI<?, ?> self)
		{
			var parentAI = self.getParentAI();

			if (parentAI.getSelectedAI() instanceof CustomizedAIAttack attack)
			{
				cir.setReturnValue((int) attack.getHorizontalSearchRange(parentAI.getAIContext()));
			}
			else
			{
				cir.setReturnValue(0);
			}

		}

	}

	@Inject(method = "getYSearchRange", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void getYSearchRange(CallbackInfoReturnable<Integer> cir)
	{
		if (this instanceof ICustomizableAttackMoveAI<?, ?> self)
		{
			var parentAI = self.getParentAI();

			if (parentAI.getSelectedAI() instanceof CustomizedAIAttack attack)
			{
				cir.setReturnValue((int) attack.getVerticalSearchRange(parentAI.getAIContext()));
			}
			else
			{
				cir.setReturnValue(0);
			}

		}

	}

}
