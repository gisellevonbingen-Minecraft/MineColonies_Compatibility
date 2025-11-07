package steve_gall.minecolonies_compatibility.api.common.building.module;

import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;

import net.minecraft.network.chat.Component;

public interface IMenuBuildingModuleView extends IBuildingModuleView
{
	Component getMenuDesc();
}
