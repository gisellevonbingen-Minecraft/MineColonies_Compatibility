package steve_gall.minecolonies_compatibility.core.common.colony.jobs;

import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.core.colony.jobs.AbstractJobGuard;

import net.minecraft.resources.ResourceLocation;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.citizen.guard.EntityAIGunner;

public class JobGunner extends AbstractJobGuard<JobGunner>
{
	public JobGunner(ICitizenData entity)
	{
		super(entity);
	}

	@Override
	public EntityAIGunner generateGuardAI()
	{
		return new EntityAIGunner(this);
	}

	@Override
	public ResourceLocation getModel()
	{
		return ModModelTypes.ARCHER_GUARD_ID;
	}

}
