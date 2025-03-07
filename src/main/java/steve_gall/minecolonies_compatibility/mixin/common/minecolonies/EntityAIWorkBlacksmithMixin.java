package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.core.Network;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingBlacksmith;
import com.minecolonies.core.colony.jobs.JobBlacksmith;
import com.minecolonies.core.entity.ai.workers.crafting.AbstractEntityAICrafting;
import com.minecolonies.core.entity.ai.workers.crafting.EntityAIWorkBlacksmith;
import com.minecolonies.core.network.messages.client.LocalizedParticleEffectMessage;

import steve_gall.minecolonies_compatibility.api.common.entity.ai.AIInterruptEventTarget;
import steve_gall.minecolonies_compatibility.api.common.repair.CustomizedRepair;
import steve_gall.minecolonies_compatibility.api.common.repair.EntityContext;
import steve_gall.minecolonies_compatibility.api.common.repair.RepairTransaction;
import steve_gall.minecolonies_compatibility.api.common.repair.RepairTransaction.RepairResult;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AIRepairState;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;

@Mixin(value = EntityAIWorkBlacksmith.class)
public abstract class EntityAIWorkBlacksmithMixin extends AbstractEntityAICrafting<JobBlacksmith, BuildingBlacksmith>
{
	@Unique
	private RepairTransaction minecolonies_compatibility$transaction;
	@Unique
	private int minecolonies_compatibility$hitCount;

	public EntityAIWorkBlacksmithMixin(@NotNull JobBlacksmith job)
	{
		super(job);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Inject(method = "<init>", remap = false, at = @At(value = "TAIL"), cancellable = false)
	private void init(JobBlacksmith blacksmith, CallbackInfo ci)
	{
		if (ModuleManager.TCONSTRUCT.isLoaded())
		{
			this.registerTarget(new AIInterruptEventTarget(() ->
			{
				var state = this.getState();
				return state == AIWorkerState.IDLE || state == AIWorkerState.START_WORKING;
			}, this::checkRepairableItem, 20));
			this.registerTarget(new AITarget(AIRepairState.REPAIR, this::repair, HIT_DELAY));
		}

	}

	private IAIState checkRepairableItem()
	{
		if (!this.building.getSettingValueOrDefault(ModBuildingModules.REPAIR_ITEM, false))
		{
			return null;
		}

		var context = new EntityContext((EntityAIWorkBlacksmith) (Object) this, this.worker);

		for (var repair : CustomizedRepair.getRegistry().values())
		{
			var result = repair.check(context);

			if (result == null)
			{
				continue;
			}
			else if (result.transaction != null)
			{
				this.minecolonies_compatibility$transaction = result.transaction;
				this.minecolonies_compatibility$hitCount = 0;
				return AIRepairState.REPAIR;
			}
			else if (result.needsCurrently != null)
			{
				this.needsCurrently = result.needsCurrently;
				return AIWorkerState.GATHERING_REQUIRED_MATERIALS;
			}

		}

		return null;
	}

	private IAIState repair()
	{
		if (this.minecolonies_compatibility$transaction == null)
		{
			return AIWorkerState.START_WORKING;
		}
		else if (!this.walkToBuilding())
		{
			return this.getState();
		}

		var context = new EntityContext((EntityAIWorkBlacksmith) (Object) this, this.worker);

		if (!this.minecolonies_compatibility$transaction.onHitting(context))
		{
			return this.onHitComplete(context, null);
		}

		this.minecolonies_compatibility$hitCount++;
		var mainHeld = this.worker.getMainHandItem();
		this.hitBlockWithToolInHand(this.building.getPosition());

		if (!mainHeld.isEmpty())
		{
			Network.getNetwork().sendToTrackingEntity(new LocalizedParticleEffectMessage(mainHeld, this.building.getPosition().above()), this.worker);
		}

		if (this.minecolonies_compatibility$hitCount >= ((AbstractEntityAICraftingAccessor) this).invokeGetRequiredProgressForMakingRawMaterial())
		{
			var result = this.minecolonies_compatibility$transaction.onHitComplete(context);
			return this.onHitComplete(context, result);
		}
		else
		{
			return this.getState();
		}

	}

	private IAIState onHitComplete(EntityContext context, RepairResult result)
	{
		if (result != null)
		{
			if (result.state == RepairResult.State.NEXT)
			{
				if (result.next != null)
				{
					this.worker.decreaseSaturationForContinuousAction();
					this.minecolonies_compatibility$transaction = result.next;
					this.minecolonies_compatibility$hitCount = 0;
				}

				return this.getState();
			}
			else if (result.state == RepairResult.State.COMPLETED)
			{
				context.setHands(-1, -1);
				this.incrementActionsDoneAndDecSaturation();
				this.worker.getCitizenExperienceHandler().addExperience(0.5D);
				this.minecolonies_compatibility$transaction = null;
				return AIWorkerState.INVENTORY_FULL;
			}

		}

		{
			context.setHands(-1, -1);
			this.minecolonies_compatibility$transaction = null;
			return AIWorkerState.START_WORKING;
		}

	}

}
