package steve_gall.minecolonies_compatibility.module.common.tconstruct.init;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;

import steve_gall.minecolonies_compatibility.module.common.tconstruct.building.module.RepairMaterialListModule;

public class ModuleBuildingModules
{
	public static final BuildingEntry.ModuleProducer<RepairMaterialListModule, RepairMaterialListModule.View> REPAIR_MATERIALS = new BuildingEntry.ModuleProducer<>("tconstruct_repair_materials", //
			() -> new RepairMaterialListModule(), //
			() -> RepairMaterialListModule.View::new);//

	private ModuleBuildingModules()
	{

	}

}
