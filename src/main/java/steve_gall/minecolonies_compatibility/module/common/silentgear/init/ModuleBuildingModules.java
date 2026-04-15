package steve_gall.minecolonies_compatibility.module.common.silentgear.init;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;

import steve_gall.minecolonies_compatibility.module.common.silentgear.building.module.RepairMaterialListModule;

public class ModuleBuildingModules
{
	public static final BuildingEntry.ModuleProducer<RepairMaterialListModule, RepairMaterialListModule.View> REPAIR_MATERIALS = new BuildingEntry.ModuleProducer<>("silentgear_repair_materials", //
			() -> new RepairMaterialListModule(), //
			() -> RepairMaterialListModule.View::new);//

	private ModuleBuildingModules()
	{

	}

}
