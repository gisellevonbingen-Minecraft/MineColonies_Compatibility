package steve_gall.minecolonies_compatibility.api.common.entity.ai.guard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.api.entity.combat.CombatAIStates;
import com.minecolonies.api.entity.pathfinding.PathResult;
import com.minecolonies.api.util.constant.GuardConstants;
import com.minecolonies.api.util.constant.StatisticsConstants;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.colony.buildings.modules.BuildingModules;
import com.minecolonies.core.colony.jobs.AbstractJobGuard;
import com.minecolonies.core.entity.ai.citizen.guard.AbstractEntityAIGuard;
import com.minecolonies.core.entity.ai.combat.AttackMoveAI;
import com.minecolonies.core.entity.ai.combat.CombatUtils;
import com.minecolonies.core.entity.citizen.EntityCitizen;

import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.ICustomizableEntityAI;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.ICustomizableStateAI;

public abstract class CustomizableAISimpleGuard<T extends AbstractEntityAIGuard<J, B> & ICustomizableEntityAI, J extends AbstractJobGuard<J>, B extends AbstractBuildingGuards> extends AttackMoveAI<EntityCitizen> implements ICustomizableStateAI<T>
{
	@NotNull
	private final T parentAI;

	@Nullable
	public abstract PathResult<?> createPathResult(@NotNull LivingEntity target, double speed);

	public CustomizableAISimpleGuard(EntityCitizen owner, ITickRateStateMachine<?> stateMachine, @NotNull T parentAI)
	{
		super(owner, stateMachine);

		this.parentAI = parentAI;
	}

	@Override
	public boolean canAttack()
	{
		if (this.getParentAI().getSelectedAI() instanceof CustomizedAIGuard guard)
		{
			return guard.canAttack(this.user, this.target);
		}

		return false;
	}

	@Override
	protected void doAttack(LivingEntity target)
	{
		if (this.getParentAI().getSelectedAI() instanceof CustomizedAIGuard guard)
		{
			var user = this.user;
			user.lookAt(target, (float) GuardConstants.TURN_AROUND, (float) GuardConstants.TURN_AROUND);
			guard.doAttack(user, target);

			target.setLastHurtByMob(user);
			user.swing(InteractionHand.MAIN_HAND);
			user.getNavigation().stop();

			user.getCitizenItemHandler().damageItemInHand(InteractionHand.MAIN_HAND, 1);
			user.decreaseSaturationForContinuousAction();
		}

	}

	@Override
	protected IState tryAttack()
	{
		var state = super.tryAttack();

		if (state == CombatAIStates.NO_TARGET && this.target != null)
		{
			return null;
		}

		return state;
	}

	@Override
	protected boolean skipSearch(LivingEntity entity)
	{
		// Found a sleeping guard nearby
		if (entity instanceof EntityCitizen citizen && citizen.getCitizenJobHandler().getColonyJob() instanceof AbstractJobGuard<?> jobGuard)
		{
			if (jobGuard.isAsleep() && this.user.getSensing().hasLineOfSight(citizen))
			{
				this.getParentAI().setWakeCitizen(citizen);
				return true;
			}

		}

		return false;
	}

	@Override
	public void resetTarget()
	{
		var old = this.target;

		super.resetTarget();

		if (old != null)
		{
			if (this.getParentAI().getSelectedAI() instanceof CustomizedAIGuard guard)
			{
				guard.onTargetReset(this.user, old);
			}

		}

	}

	@Override
	protected void onTargetDied(final LivingEntity entity)
	{
		var parentAI = this.getParentAI();
		var user = this.user;

		parentAI.incrementActionsDoneAndDecSaturation();
		user.getCitizenExperienceHandler().addExperience(GuardConstants.EXP_PER_MOB_DEATH);
		user.getCitizenColonyHandler().getColony().getStatisticsManager().increment(StatisticsConstants.MOB_KILLED, user.getCitizenColonyHandler().getColony().getDay());

		if (entity.getType().getDescription().getContents() instanceof TranslatableContents translatableContents)
		{
			parentAI.building.getModule(BuildingModules.STATS_MODULE).increment(StatisticsConstants.MOB_KILLED + ";" + translatableContents.getKey());
		}

		if (parentAI.getSelectedAI() instanceof CustomizedAIGuard guard)
		{
			guard.onTargetReset(user, entity);
		}

	}

	@Override
	protected boolean isAttackableTarget(LivingEntity entity)
	{
		return AbstractEntityAIGuard.isAttackableTarget(this.user, entity);
	}

	@Override
	protected void onTargetChange()
	{
		CombatUtils.notifyGuardsOfTarget(this.user, this.target, AbstractEntityAIGuard.PATROL_DEVIATION_RAID_POINT);

		if (this.getParentAI().getSelectedAI() instanceof CustomizedAIGuard guard)
		{
			guard.onTargetChange(this.user, this.target);
		}

	}

	@Override
	protected int getAttackDelay()
	{
		if (this.getParentAI().getSelectedAI() instanceof CustomizedAIGuard guard)
		{
			var attackDelay = guard.getAttackDelay(this.user, this.target);
			return Math.max(attackDelay, GuardConstants.PHYSICAL_ATTACK_DELAY_MIN);
		}

		return super.getAttackDelay();
	}

	@Override
	protected double getAttackDistance()
	{
		if (this.getParentAI().getSelectedAI() instanceof CustomizedAIGuard guard)
		{
			return guard.getAttackDistance(this.user, this.target);
		}

		return super.getAttackDistance();
	}

	@Override
	protected int getSearchRange()
	{
		if (this.getParentAI().getSelectedAI() instanceof CustomizedAIGuard guard)
		{
			return (int) guard.getHorizontalSearchRange(this.user);
		}

		return super.getSearchRange();
	}

	@Override
	protected int getYSearchRange()
	{
		if (this.getParentAI().getSelectedAI() instanceof CustomizedAIGuard guard)
		{
			return (int) guard.getVerticalSearchRange(this.user);
		}

		return super.getYSearchRange();
	}

	@Override
	protected boolean isWithinPersecutionDistance(LivingEntity target)
	{
		var parentAI = this.getParentAI();

		if (parentAI.getSelectedAI() instanceof CustomizedAIGuard guard)
		{
			return parentAI.isWithinPersecutionDistance(target.blockPosition(), guard.getAttackDistance(this.user, target));
		}

		return super.isWithinPersecutionDistance(target);
	}

	@Override
	protected PathResult<?> moveInAttackPosition(LivingEntity target)
	{
		if (this.getParentAI().getSelectedAI() instanceof CustomizedAIGuard guard)
		{
			var speed = guard.getJobPathSpeed(this.user);
			return this.createPathResult(target, speed);
		}

		return super.moveInAttackPosition(target);
	}

	@Override
	@NotNull
	public T getParentAI()
	{
		return this.parentAI;
	}

}
