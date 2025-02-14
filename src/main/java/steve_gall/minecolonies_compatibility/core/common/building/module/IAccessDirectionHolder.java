package steve_gall.minecolonies_compatibility.core.common.building.module;

import org.jetbrains.annotations.NotNull;

public interface IAccessDirectionHolder
{
	@NotNull
	AccessDirection getAccessDirection();

	void setAccessDirection(@NotNull AccessDirection value);
}
