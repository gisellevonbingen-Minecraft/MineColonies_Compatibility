package steve_gall.minecolonies_compatibility.core.common.mixin.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.entity.ai.combat.threat.IThreatTableEntity;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.core.entity.ai.combat.AttackMoveAI;
import com.minecolonies.core.entity.ai.combat.TargetAI;

import net.minecraft.world.entity.Mob;
import steve_gall.minecolonies_compatibility.api.common.entity.CustomizedCitizenAIAttack;
import steve_gall.minecolonies_compatibility.api.common.entity.ICustomizableStateAI;

@Mixin(value = AttackMoveAI.class, remap = false)
public class AttackMoveAIMixin<T extends Mob & IThreatTableEntity> extends TargetAI<T>
{
	public AttackMoveAIMixin(T user, int targetFrequency, ITickRateStateMachine<?> stateMachine)
	{
		super(user, targetFrequency, stateMachine);
	}

	@Inject(method = "move", at = @At(value = "HEAD"), cancellable = true)
	private void move(CallbackInfoReturnable<IState> cir)
	{
		if (this instanceof ICustomizableStateAI<?> self)
		{
			var parentAI = self.getParentAI();

			if (parentAI.getSelectedAI() instanceof CustomizedCitizenAIAttack attack)
			{
				if (!attack.canTryMoveToAttack(parentAI.getAIContext()))
				{
					cir.setReturnValue(null);
				}

			}

		}

	}

}
