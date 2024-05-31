package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.colony.jobs.JobKnight;
import com.minecolonies.core.entity.ai.citizen.guard.AbstractEntityAIGuard;
import com.minecolonies.core.entity.ai.citizen.guard.EntityAIKnight;

import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

@Mixin(value = EntityAIKnight.class, remap = false)
public abstract class EntityAIKnightMixin extends AbstractEntityAIGuard<JobKnight, AbstractBuildingGuards>
{
	public EntityAIKnightMixin(@NotNull JobKnight job)
	{
		super(job);
	}

	@Inject(method = "<init>", remap = false, at = @At(value = "TAIL"), cancellable = false)
	private void init(JobKnight job, CallbackInfo ci)
	{
		if (this.toolsNeeded.remove(ToolType.SWORD))
		{
			this.toolsNeeded.add(ModToolTypes.KNIGHT_WEAPON.getToolType());
		}

	}

}
