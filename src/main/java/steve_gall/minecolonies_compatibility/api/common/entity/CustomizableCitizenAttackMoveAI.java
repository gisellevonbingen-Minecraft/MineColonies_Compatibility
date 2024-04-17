package steve_gall.minecolonies_compatibility.api.common.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.api.entity.pathfinding.PathResult;
import com.minecolonies.api.util.constant.GuardConstants;
import com.minecolonies.core.entity.ai.combat.AttackMoveAI;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.minecolonies.core.entity.pathfinding.MinecoloniesAdvancedPathNavigate;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public abstract class CustomizableCitizenAttackMoveAI<T extends ICustomizableEntityAI> extends AttackMoveAI<EntityCitizen> implements ICustomizableStateAI<T>
{
	private final T parentAI;

	public CustomizableCitizenAttackMoveAI(EntityCitizen owner, ITickRateStateMachine<?> stateMachine, T parentAI)
	{
		super(owner, stateMachine);

		this.parentAI = parentAI;
	}

	@Override
	@NotNull
	public T getParentAI()
	{
		return this.parentAI;
	}

	@Override
	protected IState tryAttack()
	{
		var parentAI = this.getParentAI();

		if (parentAI.getSelectedAI() instanceof CustomizedCitizenAIAttack attack)
		{
			if (attack.canTryAttack(parentAI.getAIContext()))
			{
				return super.tryAttack();
			}

		}

		return null;
	}

	@Override
	public boolean canAttack()
	{
		var parentAI = this.getParentAI();

		if (parentAI.getSelectedAI() instanceof CustomizedCitizenAIAttack attack)
		{
			return attack.canAttack(parentAI.getAIContext(), this.target);
		}
		else
		{
			return false;
		}

	}

	@Override
	protected void doAttack(LivingEntity target)
	{
		var parentAI = this.getParentAI();

		if (parentAI.getSelectedAI() instanceof CustomizedCitizenAIAttack attack)
		{
			attack.doAttack(parentAI.getAIContext(), target);
		}

		EntityCitizen user = this.user;
		target.setLastHurtByMob(user);
		user.swing(InteractionHand.MAIN_HAND);
		user.getCitizenItemHandler().damageItemInHand(InteractionHand.MAIN_HAND, 1);
		user.decreaseSaturationForContinuousAction();
	}

	@Override
	protected int getAttackDelay()
	{
		var parentAI = this.getParentAI();

		if (parentAI.getSelectedAI() instanceof CustomizedCitizenAIAttack attack)
		{
			var attackDelay = attack.getAttackDelay(parentAI.getAIContext(), this.target);
			return Math.max(attackDelay, GuardConstants.PHYSICAL_ATTACK_DELAY_MIN);
		}
		else
		{
			return 0;
		}

	}

	@Override
	protected double getAttackDistance()
	{
		var parentAI = this.getParentAI();

		if (parentAI.getSelectedAI() instanceof CustomizedCitizenAIAttack attack)
		{
			return attack.getAttackDistance(parentAI.getAIContext(), this.target);
		}
		else
		{
			return 0.0D;
		}

	}

	@Override
	protected int getSearchRange()
	{
		var parentAI = this.getParentAI();

		if (parentAI.getSelectedAI() instanceof CustomizedCitizenAIAttack attack)
		{
			return attack.getHorizontalSearchRange(parentAI.getAIContext());
		}
		else
		{
			return 0;
		}

	}

	@Override
	protected int getYSearchRange()
	{
		var parentAI = this.getParentAI();

		if (parentAI.getSelectedAI() instanceof CustomizedCitizenAIAttack attack)
		{
			return attack.getVerticalSearchRange(parentAI.getAIContext());
		}
		else
		{
			return 0;
		}

	}

	@Override
	protected PathResult<?> moveInAttackPosition(LivingEntity target)
	{
		var parentAI = this.getParentAI();

		if (parentAI.getSelectedAI() instanceof CustomizedCitizenAIAttack attack)
		{
			var speed = attack.getCombatMovementSpeed(parentAI.getAIContext());
			var min = MinecoloniesAdvancedPathNavigate.MIN_SPEED_ALLOWED;
			var max = MinecoloniesAdvancedPathNavigate.MAX_SPEED_ALLOWED;
			speed = Mth.clamp(speed, min, max);

			return this.createPathResult(target, this.user, speed);
		}

		else
		{
			return null;
		}

	}

	@Nullable
	protected PathResult<?> createPathResult(LivingEntity target, EntityCitizen user, double speed)
	{
		return super.moveInAttackPosition(target);
	}

}
