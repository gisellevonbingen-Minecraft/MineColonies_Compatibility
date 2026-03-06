package steve_gall.minecolonies_compatibility.module.common.cgm.init;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.colony.jobs.ModJobs;

import steve_gall.minecolonies_compatibility.module.common.cgm.building.module.WorkbenchCraftingModule;

public class ModuleBuildingModules
{
	public static final BuildingEntry.ModuleProducer<WorkbenchCraftingModule, WorkbenchCraftingModule.View> BLACKSMITH_WORKBENCH = new BuildingEntry.ModuleProducer<>("blacksmith_cgm_workben", //
			() -> new WorkbenchCraftingModule(ModJobs.blacksmith.get()), //
			() -> WorkbenchCraftingModule.View::new);//

	private ModuleBuildingModules()
	{

	}

}
