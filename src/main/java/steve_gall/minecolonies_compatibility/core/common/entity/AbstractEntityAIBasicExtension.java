package steve_gall.minecolonies_compatibility.core.common.entity;

import org.jetbrains.annotations.Nullable;

import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAIContext;

public interface AbstractEntityAIBasicExtension
{
	@Nullable
	default CustomizedAI minecolonies_compatibility$getSelectedAI()
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
