package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.colony.jobs.ModJobs;

import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.building.modules.ApplePressFermentingCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.building.modules.ApplePressFermentingCraftingModuleView;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.building.modules.ApplePressMashingCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.building.modules.ApplePressMashingCraftingModuleView;

public class ModuleBuildingModules
{
	public static final BuildingEntry.ModuleProducer<ApplePressMashingCraftingModule, ApplePressMashingCraftingModuleView> FARMER_APPLE_PRESS_MASHING = new BuildingEntry.ModuleProducer<>("farmer_lets_do_vinery_apple_press", //
			() -> new ApplePressMashingCraftingModule(ModJobs.farmer.get()), //
			() -> ApplePressMashingCraftingModuleView::new);//

	public static final BuildingEntry.ModuleProducer<ApplePressFermentingCraftingModule, ApplePressFermentingCraftingModuleView> FARMER_APPLE_PRESS_FERMENTING = new BuildingEntry.ModuleProducer<>("farmer_lets_do_vinery_apple_press_fermenting", //
			() -> new ApplePressFermentingCraftingModule(ModJobs.farmer.get()), //
			() -> ApplePressFermentingCraftingModuleView::new);//

	private ModuleBuildingModules()
	{

	}

}
