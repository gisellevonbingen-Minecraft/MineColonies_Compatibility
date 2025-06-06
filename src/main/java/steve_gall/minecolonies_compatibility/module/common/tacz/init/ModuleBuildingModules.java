package steve_gall.minecolonies_compatibility.module.common.tacz.init;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.colony.jobs.ModJobs;

import steve_gall.minecolonies_compatibility.module.common.tacz.building.module.GunSmithTableCraftingModule;

public class ModuleBuildingModules
{
	public static final BuildingEntry.ModuleProducer<GunSmithTableCraftingModule, GunSmithTableCraftingModule.View> BLACKSMITH_GUN_SMITH_TABLE = new BuildingEntry.ModuleProducer<>("blacksmith_tacz_gun_smith_table", //
			() -> new GunSmithTableCraftingModule(ModJobs.blacksmith.get()), //
			() -> GunSmithTableCraftingModule.View::new);//

	private ModuleBuildingModules()
	{

	}

}
