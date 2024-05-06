package steve_gall.minecolonies_compatibility.core.common.mixin.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.api.entity.combat.CombatAIStates;
import com.minecolonies.api.entity.combat.threat.IThreatTableEntity;
import com.minecolonies.api.entity.pathfinding.PathResult;
import com.minecolonies.api.util.constant.GuardConstants;
import com.minecolonies.core.entity.ai.citizen.guard.AbstractEntityAIGuard;
import com.minecolonies.core.entity.ai.combat.AttackMoveAI;
import com.minecolonies.core.entity.ai.combat.TargetAI;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.minecolonies.core.entity.pathfinding.MinecoloniesAdvancedPathNavigate;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAIAttack;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.ICustomizableAttackMoveAI;

@Mixin(value = AttackMoveAI.class, remap = false)
public abstract class AttackMoveAIMixin<T extends Mob & IThreatTableEntity> extends TargetAI<T>
{
	public AttackMoveAIMixin(T user, int targetFrequency, ITickRateStateMachine<?> stateMachine)
	{
		super(user, targetFrequency, stateMachine);
	}

	@Shadow
	public abstract double getAttackDistance();

	@Inject(method = "move", at = @At(value = "RETURN"), cancellable = true)
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

	@Inject(method = "tryAttack", at = @At(value = "RETURN"), cancellable = true)
	private void tryAttack_return(CallbackInfoReturnable<IState> cir)
	{
		if (cir.getReturnValue() == CombatAIStates.NO_TARGET)
		{
			if (this.target != null)
			{
				cir.setReturnValue(null);
			}

		}

	}

	@Redirect(method = "move", at = @At(value = "INVOKE", target = "checkForTarget"))
	private boolean move_checkForTarget(AttackMoveAI<T> ai)
	{
		if (!this.checkForTarget())
		{
			return false;
		}
		else if (this instanceof ICustomizableAttackMoveAI<?, ?> self)
		{
			var parentAI = self.getParentAI();

			if (parentAI.getSelectedAI() instanceof CustomizedAIAttack attack)
			{
				return attack.canAttack(parentAI.getAIContext(), this.target);
			}

		}

		return true;
	}

	@Inject(method = "canAttack", at = @At(value = "HEAD"), cancellable = true)
	private void canAttack(CallbackInfoReturnable<Boolean> cir)
	{
		if (this instanceof ICustomizableAttackMoveAI<?, ?> self)
		{
			var parentAI = self.getParentAI();

			if (parentAI.getSelectedAI() instanceof CustomizedAIAttack attack)
			{
				cir.setReturnValue(attack.canAttack(parentAI.getAIContext(), this.target));
			}
			else
			{
				cir.setReturnValue(false);
			}

		}

	}

	@Inject(method = "doAttack", at = @At(value = "HEAD"), cancellable = true)
	private void doAttack(LivingEntity target, CallbackInfo ci)
	{
		if (this instanceof ICustomizableAttackMoveAI<?, ?> self)
		{
			var parentAI = self.getParentAI();

			if (parentAI.getSelectedAI() instanceof CustomizedAIAttack attack)
			{
				attack.doAttack(parentAI.getAIContext(), target);

				var user = this.user;
				target.setLastHurtByMob(user);
				user.swing(InteractionHand.MAIN_HAND);

				if (user instanceof EntityCitizen citizen)
				{
					citizen.getCitizenItemHandler().damageItemInHand(InteractionHand.MAIN_HAND, 1);
					citizen.decreaseSaturationForContinuousAction();
				}

			}

		}

	}

	@Inject(method = "getAttackDelay", at = @At(value = "HEAD"), cancellable = true)
	private void getAttackDelay(CallbackInfoReturnable<Integer> cir)
	{
		if (this instanceof ICustomizableAttackMoveAI<?, ?> self)
		{
			var parentAI = self.getParentAI();

			if (parentAI.getSelectedAI() instanceof CustomizedAIAttack attack)
			{
				var attackDelay = attack.getAttackDelay(parentAI.getAIContext(), this.target);
				cir.setReturnValue(Math.max(attackDelay, GuardConstants.PHYSICAL_ATTACK_DELAY_MIN));
			}
			else
			{
				cir.setReturnValue(0);
			}

		}

	}

	@Inject(method = "getAttackDistance", at = @At(value = "HEAD"), cancellable = true)
	private void getAttackDistance(CallbackInfoReturnable<Double> cir)
	{
		if (this instanceof ICustomizableAttackMoveAI<?, ?> self)
		{
			var parentAI = self.getParentAI();

			if (parentAI.getSelectedAI() instanceof CustomizedAIAttack attack)
			{
				cir.setReturnValue(attack.getAttackDistance(parentAI.getAIContext(), this.target));
			}
			else
			{
				cir.setReturnValue(0.0D);
			}

		}

	}

	@SuppressWarnings("unchecked")
	@Inject(method = "moveInAttackPosition", at = @At(value = "HEAD"), cancellable = true)
	private void moveInAttackPosition(LivingEntity target, CallbackInfoReturnable<PathResult<?>> cir)
	{
		if (this instanceof ICustomizableAttackMoveAI<?, ?> self)
		{
			var parentAI = self.getParentAI();

			if (parentAI.getSelectedAI() instanceof CustomizedAIAttack attack)
			{
				var speed = attack.getCombatMovementSpeed(parentAI.getAIContext());
				var min = MinecoloniesAdvancedPathNavigate.MIN_SPEED_ALLOWED;
				var max = MinecoloniesAdvancedPathNavigate.MAX_SPEED_ALLOWED;
				speed = Mth.clamp(speed, min, max);

				cir.setReturnValue(((ICustomizableAttackMoveAI<?, T>) this).createPathResult(target, this.user, speed));
			}
			else
			{
				cir.setReturnValue(null);
			}

		}

	}

	@Override
	protected boolean isWithinPersecutionDistance(LivingEntity target)
	{
		if (this instanceof ICustomizableAttackMoveAI<?, ?> self)
		{
			var parentAI = self.getParentAI();

			if (parentAI instanceof AbstractEntityAIGuard<?, ?> guard && parentAI.getSelectedAI() instanceof CustomizedAIAttack attack)
			{
				return guard.isWithinPersecutionDistance(target.blockPosition(), attack.getAttackDistance(parentAI.getAIContext(), target));
			}

		}

		return super.isWithinPersecutionDistance(target);
	}

}
