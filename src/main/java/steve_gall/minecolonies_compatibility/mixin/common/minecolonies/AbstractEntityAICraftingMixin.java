package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.minecolonies.api.colony.buildings.modules.ICraftingBuildingModule;
import com.minecolonies.api.colony.interactionhandling.ChatPriority;
import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.colony.requestsystem.requestable.crafting.PublicCrafting;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.buildings.modules.CraftingWorkerBuildingModule;
import com.minecolonies.core.colony.jobs.AbstractJobCrafter;
import com.minecolonies.core.entity.ai.basic.AbstractEntityAICrafting;
import com.minecolonies.core.entity.ai.basic.AbstractEntityAIInteract;

import net.minecraft.core.BlockPos;
import steve_gall.minecolonies_compatibility.api.common.building.module.ICraftingModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.building.module.ICraftingResultListenerModule;
import steve_gall.minecolonies_compatibility.api.common.building.module.ICraftingTimeOverrideModule;
import steve_gall.minecolonies_compatibility.core.common.colony.WorkingBlockInteraction;

@Mixin(value = AbstractEntityAICrafting.class, remap = false)
public abstract class AbstractEntityAICraftingMixin<J extends AbstractJobCrafter<?, J>, B extends AbstractBuilding> extends AbstractEntityAIInteract<J, B>
{
	@Shadow(remap = false)
	private IRequest<? extends PublicCrafting> currentRequest;
	@Shadow(remap = false)
	private IRecipeStorage currentRecipeStorage;

	@Unique
	private ICraftingBuildingModule minecolonies_compatibility$craftingModule;
	@Unique
	private BlockPos minecolonies_compatibility$workingPosition;
	@Unique
	private BlockPos minecolonies_compatibility$hitPosition;
	@Unique
	private BlockPos minecolonies_compatibility$particlePosition;

	public AbstractEntityAICraftingMixin(@NotNull J job)
	{
		super(job);
	}

	@Inject(method = "getRecipe", remap = false, at = @At(value = "HEAD"), cancellable = false)
	private void getRecipe_head(CallbackInfoReturnable<IAIState> cir)
	{
		this.minecolonies_compatibility$craftingModule = null;
	}

	@Inject(method = "getRecipe", remap = false, at = @At(value = "TAIL"), cancellable = false)
	private void getRecipe_tail(CallbackInfoReturnable<IAIState> cir, @Local ICraftingBuildingModule module)
	{
		this.minecolonies_compatibility$craftingModule = module;
	}

	@WrapOperation(method = "craft", remap = false, at = @At(value = "INVOKE", target = "walkToBuilding"))
	protected boolean craft_walkToBuilding(AbstractEntityAICrafting<J, B> self, Operation<Boolean> operation)
	{
		var request = this.currentRequest;
		var recipeStorage = this.currentRecipeStorage;

		if (this.minecolonies_compatibility$craftingModule instanceof ICraftingModuleWithExternalWorkingBlocks module && module.needWorkingBlock(recipeStorage))
		{
			module.requestFindWorkingBlocks(this.worker);
			var pos = module.getRecipeWorkingBlocks(recipeStorage).findAny().orElse(null);

			if (pos != null)
			{
				this.minecolonies_compatibility$workingPosition = pos;
				this.minecolonies_compatibility$hitPosition = module.getHitPosition(pos);
				this.minecolonies_compatibility$particlePosition = module.getParticlePosition(pos);
				return this.walkToBlock(module.getWalkingPosition(pos));
			}
			else
			{
				this.worker.getCitizenData().triggerInteraction(new WorkingBlockInteraction(ChatPriority.BLOCKING, module, recipeStorage, request));
				this.walkToBuilding();
				return true;
			}

		}

		this.minecolonies_compatibility$workingPosition = null;
		return operation.call(self);
	}

	@WrapOperation(method = "craft", remap = false, at = @At(value = "INVOKE", target = "Lcom/minecolonies/core/colony/buildings/AbstractBuilding;getPosition()Lnet/minecraft/core/BlockPos;", ordinal = 0))
	private BlockPos craft_building_getPosition_0(B building, Operation<BlockPos> operation)
	{
		if (this.minecolonies_compatibility$workingPosition != null)
		{
			return this.minecolonies_compatibility$hitPosition;
		}
		else
		{
			return operation.call(building);
		}

	}

	@WrapOperation(method = "craft", remap = false, at = @At(value = "INVOKE", target = "Lcom/minecolonies/core/colony/buildings/AbstractBuilding;getPosition()Lnet/minecraft/core/BlockPos;", ordinal = 1))
	private BlockPos craft_building_getPosition_1(B building, Operation<BlockPos> operation)
	{
		if (this.minecolonies_compatibility$workingPosition != null)
		{
			return this.minecolonies_compatibility$particlePosition;
		}
		else
		{
			return operation.call(building);
		}

	}

	@Inject(method = "getRequiredProgressForMakingRawMaterial", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void getRequiredProgressForMakingRawMaterial(CallbackInfoReturnable<Integer> cir)
	{
		var recipeStorage = this.currentRecipeStorage;

		if (this.minecolonies_compatibility$craftingModule instanceof ICraftingTimeOverrideModule module)
		{
			var craftingTime = module.getCraftingTime(this.worker, minecolonies_compatibility$workingPosition, recipeStorage);

			if (craftingTime >= 0)
			{
				var craftSpeedSkill = ((CraftingWorkerBuildingModule) this.getModuleForJob()).getCraftSpeedSkill();
				var speed = 1.0D - ((this.worker.getCitizenData().getCitizenSkillHandler().getLevel(craftSpeedSkill) + 1) / 200.0D);
				var hitDelay = AbstractEntityAICraftingAccessor.getHitDelay();
				cir.setReturnValue((int) Math.ceil((craftingTime * speed) / hitDelay));
			}

		}

	}

	@Inject(method = "craft", remap = false, at = @At(value = "INVOKE", target = "Lcom/minecolonies/api/colony/requestsystem/request/IRequest;addDelivery(Lnet/minecraft/world/item/ItemStack;)V"), cancellable = false)
	private void craft_addDelivery(CallbackInfoReturnable<IAIState> cir)
	{
		var recipeStorage = this.currentRecipeStorage;

		if (this.minecolonies_compatibility$craftingModule instanceof ICraftingResultListenerModule module)
		{
			var pos = this.minecolonies_compatibility$workingPosition != null ? this.minecolonies_compatibility$workingPosition : this.building.getPosition();
			module.onCrafted(this.worker, pos, recipeStorage);
		}

	}

}
