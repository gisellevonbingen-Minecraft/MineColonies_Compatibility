package steve_gall.minecolonies_compatibility.api.common.entity.guard;

import static com.minecolonies.api.util.constant.GuardConstants.EXP_PER_MOB_DEATH;
import static com.minecolonies.api.util.constant.StatisticsConstants.MOBS_KILLED;
import static com.minecolonies.api.util.constant.StatisticsConstants.MOB_KILLED;
import static com.minecolonies.core.colony.buildings.modules.BuildingModules.STATS_MODULE;
import static com.minecolonies.core.entity.ai.workers.guard.AbstractEntityAIGuard.PATROL_DEVIATION_RAID_POINT;

import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.colony.jobs.AbstractJobGuard;
import com.minecolonies.core.entity.ai.combat.CombatUtils;
import com.minecolonies.core.entity.ai.workers.guard.AbstractEntityAIGuard;
import com.minecolonies.core.entity.citizen.EntityCitizen;

import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.LivingEntity;
import steve_gall.minecolonies_compatibility.api.common.entity.CustomizableCitizenAttackMoveAI;
import steve_gall.minecolonies_compatibility.api.common.entity.ICustomizableEntityAI;

public abstract class CustomizableCitizenAttackMoveAIGuard<T extends AbstractEntityAIGuard<J, B> & ICustomizableEntityAI, J extends AbstractJobGuard<J>, B extends AbstractBuildingGuards> extends CustomizableCitizenAttackMoveAI<T>
{
	public CustomizableCitizenAttackMoveAIGuard(EntityCitizen owner, ITickRateStateMachine<?> stateMachine, T parentAI)
	{
		super(owner, stateMachine, parentAI);
	}

	@Override
	protected boolean isWithinPersecutionDistance(LivingEntity target)
	{
		return this.getParentAI().isWithinPersecutionDistance(target.blockPosition(), this.getAttackDistance());
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
	protected void onTargetDied(final LivingEntity entity)
	{
		var parentAI = this.getParentAI();
		var user = this.user;

		parentAI.incrementActionsDoneAndDecSaturation();
		user.getCitizenExperienceHandler().addExperience(EXP_PER_MOB_DEATH);
		user.getCitizenColonyHandler().getColony().getStatisticsManager().increment(MOBS_KILLED, user.getCitizenColonyHandler().getColony().getDay());

		if (entity.getType().getDescription().getContents() instanceof TranslatableContents translatableContents)
		{
			parentAI.building.getModule(STATS_MODULE).increment(MOB_KILLED + ";" + translatableContents.getKey());
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
		CombatUtils.notifyGuardsOfTarget(this.user, this.target, PATROL_DEVIATION_RAID_POINT);
	}

}
