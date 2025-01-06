package steve_gall.minecolonies_compatibility.core.common.job;

import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.core.colony.jobs.AbstractJob;

import net.minecraft.resources.ResourceLocation;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.butcher.EntityAIWorkButcher;

public class JobButcher extends AbstractJob<EntityAIWorkButcher, JobButcher>
{
	public JobButcher(ICitizenData entity)
	{
		super(entity);
	}

	@Override
	public void onLevelUp()
	{

	}

	@Override
	public EntityAIWorkButcher generateAI()
	{
		return new EntityAIWorkButcher(this);
	}

	@Override
	public ResourceLocation getModel()
	{
		return ModModelTypes.COOK_ID;
	}

}
