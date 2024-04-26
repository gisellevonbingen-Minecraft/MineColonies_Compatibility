package steve_gall.minecolonies_compatibility.api.common.entity.guard;

import static com.minecolonies.api.util.constant.GuardConstants.EXP_PER_MOB_DEATH;
import static com.minecolonies.api.util.constant.StatisticsConstants.MOBS_KILLED;
import static com.minecolonies.api.util.constant.StatisticsConstants.MOB_KILLED;
import static com.minecolonies.core.colony.buildings.modules.BuildingModules.STATS_MODULE;
import static com.minecolonies.core.entity.ai.workers.guard.AbstractEntityAIGuard.PATROL_DEVIATION_RAID_POINT;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.colony.jobs.AbstractJobGuard;
import com.minecolonies.core.entity.ai.combat.AttackMoveAI;
import com.minecolonies.core.entity.ai.combat.CombatUtils;
import com.minecolonies.core.entity.ai.workers.guard.AbstractEntityAIGuard;
import com.minecolonies.core.entity.citizen.EntityCitizen;

import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.LivingEntity;
import steve_gall.minecolonies_compatibility.api.common.entity.ICustomizableAttackMoveAI;
import steve_gall.minecolonies_compatibility.api.common.entity.ICustomizableEntityAI;

public abstract class CustomizableCitizenAISimpleGuard<T extends AbstractEntityAIGuard<J, B> & ICustomizableEntityAI, J extends AbstractJobGuard<J>, B extends AbstractBuildingGuards> extends AttackMoveAI<EntityCitizen> implements ICustomizableAttackMoveAI<T, EntityCitizen>
{
	@NotNull
	private final T parentAI;

	public CustomizableCitizenAISimpleGuard(EntityCitizen owner, ITickRateStateMachine<?> stateMachine, @NotNull T parentAI)
	{
		super(owner, stateMachine);

		this.parentAI = parentAI;
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

	@Override
	@NotNull
	public T getParentAI()
	{
		return this.parentAI;
	}

}
