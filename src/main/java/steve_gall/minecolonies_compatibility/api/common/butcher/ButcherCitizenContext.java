package steve_gall.minecolonies_compatibility.api.common.butcher;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import steve_gall.minecolonies_compatibility.core.common.entity.ai.butcher.EntityAIWorkButcher;

public class ButcherCitizenContext
{
	@NotNull
	private final EntityAIWorkButcher ai;
	@NotNull
	private final AbstractEntityCitizen worker;

	public ButcherCitizenContext(@NotNull EntityAIWorkButcher ai, @NotNull AbstractEntityCitizen worker)
	{
		this.ai = ai;
		this.worker = worker;
	}

	@NotNull
	public EntityAIWorkButcher getAI()
	{
		return this.ai;
	}

	@NotNull
	public AbstractEntityCitizen getWorker()
	{
		return this.worker;
	}

}
