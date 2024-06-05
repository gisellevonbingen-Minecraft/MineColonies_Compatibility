package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecolonies.api.colony.interactionhandling.ChatPriority;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.interactionhandling.StandardInteraction;
import com.minecolonies.core.colony.jobs.AbstractJobCrafter;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIInteract;
import com.minecolonies.core.entity.ai.workers.crafting.AbstractEntityAICrafting;

import net.minecraft.core.BlockPos;
import steve_gall.minecolonies_compatibility.api.common.building.module.ICraftingModuleWithExternalWorkingBlocks;

@Mixin(value = AbstractEntityAICrafting.class, remap = false)
public abstract class AbstractEntityAICraftingMixin<J extends AbstractJobCrafter<?, J>, B extends AbstractBuilding> extends AbstractEntityAIInteract<J, B>
{
	@Shadow(remap = false)
	private IRecipeStorage currentRecipeStorage;

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

	@Redirect(method = "craft", remap = false, at = @At(value = "INVOKE", target = "walkToBuilding"))
	protected boolean craft_walkToBuilding(AbstractEntityAICrafting<J, B> self)
	{
		var recipeStorage = this.currentRecipeStorage;

		if (this.building.getCraftingModuleForRecipe(recipeStorage.getToken()) instanceof ICraftingModuleWithExternalWorkingBlocks module && module.isIntermediate(recipeStorage.getIntermediate()))
		{
			module.requestFindWorkingBlocks(this.worker);
			var pos = module.getWorkingBlocks().findAny().orElse(null);

			if (pos != null)
			{
				this.minecolonies_compatibility$workingPosition = pos;
				this.minecolonies_compatibility$hitPosition = module.getHitPosition(pos);
				this.minecolonies_compatibility$particlePosition = module.getParticlePosition(pos);
				return this.walkToBlock(module.getWalkingPosition(pos));
			}
			else
			{
				this.worker.getCitizenData().triggerInteraction(new StandardInteraction(module.getWorkingBlockNotFoundMessage(), ChatPriority.BLOCKING));
				this.walkToBuilding();
				return true;
			}

		}

		this.minecolonies_compatibility$workingPosition = null;
		return this.walkToBuilding();
	}

	@Redirect(method = "craft", remap = false, at = @At(value = "INVOKE", target = "Lcom/minecolonies/core/colony/buildings/AbstractBuilding;getPosition()Lnet/minecraft/core/BlockPos;", ordinal = 0))
	private BlockPos craft_building_getPosition_0(B building)
	{
		if (this.minecolonies_compatibility$workingPosition != null)
		{
			return this.minecolonies_compatibility$hitPosition;
		}
		else
		{
			return building.getPosition();
		}

	}

	@Redirect(method = "craft", remap = false, at = @At(value = "INVOKE", target = "Lcom/minecolonies/core/colony/buildings/AbstractBuilding;getPosition()Lnet/minecraft/core/BlockPos;", ordinal = 1))
	private BlockPos craft_building_getPosition_1(B building)
	{
		if (this.minecolonies_compatibility$workingPosition != null)
		{
			return this.minecolonies_compatibility$particlePosition;
		}
		else
		{
			return building.getPosition();
		}

	}

}
