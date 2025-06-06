package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.entity.combat.CombatAIStates;
import com.minecolonies.api.entity.combat.threat.IThreatTableEntity;
import com.minecolonies.core.entity.ai.combat.AttackMoveAI;
import com.minecolonies.core.entity.ai.combat.TargetAI;

import net.minecraft.world.entity.Mob;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.ICustomizableStateAI;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.CustomizedAIGuard;

@Mixin(value = AttackMoveAI.class, remap = false)
public abstract class AttackMoveAIMixin<T extends Mob & IThreatTableEntity> extends TargetAI<T>
{
	public AttackMoveAIMixin(T user, int targetFrequency, ITickRateStateMachine<?> stateMachine)
	{
		super(user, targetFrequency, stateMachine);
	}

	@Inject(method = "move", remap = false, at = @At(value = "RETURN"), cancellable = true)
	private void move_return(CallbackInfoReturnable<IState> cir)
	{
		if (cir.getReturnValue() == CombatAIStates.NO_TARGET)
		{
			if (this.target != null)
			{
				cir.setReturnValue(null);
			}

		}

	}

	@WrapOperation(method = "move", remap = false, at = @At(value = "INVOKE", target = "checkForTarget"))
	private boolean move_checkForTarget(AttackMoveAI<T> ai, Operation<Boolean> operation)
	{
		if (!operation.call(ai))
		{
			return false;
		}
		else if (this instanceof ICustomizableStateAI<?> self)
		{
			var parentAI = self.getParentAI();

			if (parentAI.getSelectedAI() instanceof CustomizedAIGuard guard)
			{
				return guard.canAttack((AbstractEntityCitizen) this.user, this.target);
			}

		}

		return true;
	}

}
