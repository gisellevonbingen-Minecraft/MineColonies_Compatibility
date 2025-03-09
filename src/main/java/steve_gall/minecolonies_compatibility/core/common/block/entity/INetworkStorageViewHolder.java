package steve_gall.minecolonies_compatibility.core.common.block.entity;

import org.jetbrains.annotations.NotNull;

import steve_gall.minecolonies_compatibility.api.common.building.module.INetworkStorageView;

public interface INetworkStorageViewHolder
{
	@NotNull
	INetworkStorageView getNetworkStorageView();
}
