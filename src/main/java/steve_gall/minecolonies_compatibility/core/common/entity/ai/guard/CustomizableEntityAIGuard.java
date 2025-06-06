package steve_gall.minecolonies_compatibility.core.common.entity.ai.guard;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.colony.jobs.AbstractJobGuard;
import com.minecolonies.core.entity.ai.workers.guard.AbstractEntityAIGuard;

import steve_gall.minecolonies_compatibility.api.common.entity.ai.ICustomizableEntityAI;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.CustomizedAIGuard;

public abstract class CustomizableEntityAIGuard<J extends AbstractJobGuard<J>, B extends AbstractBuildingGuards> extends AbstractEntityAIGuard<J, B> implements ICustomizableEntityAI
{
	public CustomizableEntityAIGuard(@NotNull J job)
	{
		super(job);
	}

	@Override
	protected void atBuildingActions()
	{
		super.atBuildingActions();

		if (this.getSelectedAI() instanceof CustomizedAIGuard guard)
		{
			guard.atBuildingActions(this.worker);
		}

	}

}
