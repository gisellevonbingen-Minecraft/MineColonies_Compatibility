package steve_gall.minecolonies_compatibility.core.common.entity;

import org.jetbrains.annotations.Nullable;

import steve_gall.minecolonies_compatibility.api.common.entity.CustomizedAIContext;
import steve_gall.minecolonies_compatibility.api.common.entity.CustomizedCitizenAI;

public interface AbstractEntityAIBasicExtension
{
	@Nullable
	default CustomizedCitizenAI minecolonies_compatibility$getSelectedAI()
	{
		return null;
	}

	@Nullable
	default CustomizedAIContext minecolonies_compatibility$getAIContext()
	{
		return null;
	}

	default void minecolonies_compatibility$onTick()
	{

	}

}
