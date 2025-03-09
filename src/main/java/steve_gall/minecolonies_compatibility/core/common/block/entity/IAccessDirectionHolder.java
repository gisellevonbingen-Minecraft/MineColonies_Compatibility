package steve_gall.minecolonies_compatibility.core.common.block.entity;

import org.jetbrains.annotations.NotNull;

import steve_gall.minecolonies_compatibility.core.common.building.module.AccessDirection;

public interface IAccessDirectionHolder
{
	@NotNull
	AccessDirection getAccessDirection();

	void setAccessDirection(@NotNull AccessDirection value);
}
