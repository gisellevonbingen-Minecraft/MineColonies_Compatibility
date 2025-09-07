package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.List;

import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.buildings.modules.ISettingsModule;
import com.minecolonies.api.colony.buildings.modules.settings.ISetting;
import com.minecolonies.api.colony.buildings.modules.settings.ISettingKey;
import com.minecolonies.api.colony.buildings.modules.settings.ISettingsModuleView;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry.ModuleProducer;
import com.mojang.datafixers.util.Pair;

import net.neoforged.bus.api.Event;

public class InjectBuildingSettingsModuleEvent extends Event
{
	private final IBuilding building;

	public InjectBuildingSettingsModuleEvent(IBuilding building)
	{
		this.building = building;
	}

	public IBuilding getBuilding()
	{
		return this.building;
	}

	public void register(ModuleProducer<? extends ISettingsModule, ? extends ISettingsModuleView> producer, List<Pair<ISettingKey<?>, ISetting<?>>> settings)
	{
		var module = this.building.getModule(producer);

		if (module != null)
		{
			settings.stream().forEach(pair -> module.with(pair.getFirst(), pair.getSecond()));
		}

	}

}
