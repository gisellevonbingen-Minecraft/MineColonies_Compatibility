package steve_gall.minecolonies_compatibility.module.common.farmersdelight.job;

import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.core.colony.jobs.AbstractJob;

import net.minecraft.resources.ResourceLocation;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.entity.ai.EntityAIWorkFarmersCook;

public class JobFarmersCook extends AbstractJob<EntityAIWorkFarmersCook, JobFarmersCook>
{
	public JobFarmersCook(ICitizenData entity)
	{
		super(entity);
	}

	@Override
	public EntityAIWorkFarmersCook generateAI()
	{
		return new EntityAIWorkFarmersCook(this);
	}

	@Override
	public ResourceLocation getModel()
	{
		return ModModelTypes.COOK_ID;
	}

}
