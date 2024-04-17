package steve_gall.minecolonies_compatibility.api.common.entity;

import org.jetbrains.annotations.NotNull;

public interface ICustomizableStateAI<ENTITY_AI extends ICustomizableEntityAI>
{
	@NotNull
	ENTITY_AI getParentAI();
}
