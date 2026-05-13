package steve_gall.minecolonies_compatibility.module.common.scguns.init;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.colony.jobs.ModJobs;

import steve_gall.minecolonies_compatibility.module.common.scguns.building.module.GunBenchCraftingModule;

public class ModuleBuildingModules
{
	public static final BuildingEntry.ModuleProducer<GunBenchCraftingModule, GunBenchCraftingModule.View> BLACKSMITH_GUN_BENCH = new BuildingEntry.ModuleProducer<>("blacksmith_scguns_workbench", //
			() -> new GunBenchCraftingModule(ModJobs.blacksmith.get()), //
			() -> GunBenchCraftingModule.View::new);//

	private ModuleBuildingModules()
	{

	}

}
