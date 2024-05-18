package steve_gall.minecolonies_compatibility.module.common.farmersdelight.init;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.api.entity.citizen.Skill;
import com.minecolonies.core.colony.buildings.modules.NoPrivateCrafterWorkerModule;
import com.minecolonies.core.colony.buildings.moduleviews.WorkerBuildingModuleView;

import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.CookingCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.CookingCraftingModuleView;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.CuttingCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.CuttingCraftingModuleView;

public class ModuleBuildingModules
{
	public static final BuildingEntry.ModuleProducer<CuttingCraftingModule, CuttingCraftingModuleView> COOKASSISTENT_CUTTING = new BuildingEntry.ModuleProducer<>("cookassistent_farmers_cutting", //
			() -> new CuttingCraftingModule(ModJobs.cookassistant.get(), ModToolTypes.KNIFE.getToolType()), //
			() -> CuttingCraftingModuleView::new);//

	public static final BuildingEntry.ModuleProducer<NoPrivateCrafterWorkerModule, WorkerBuildingModuleView> COOK_WORK = new BuildingEntry.ModuleProducer<>("farmers_cook_work", //
			() -> new NoPrivateCrafterWorkerModule(ModuleJobs.FARMERS_COOK.get(), Skill.Adaptability, Skill.Knowledge, true, (b) -> 1), //
			() -> WorkerBuildingModuleView::new);

	public static final BuildingEntry.ModuleProducer<CookingCraftingModule, CookingCraftingModuleView> COOK_COOKING = new BuildingEntry.ModuleProducer<>("farmers_cook_cooking", //
			() -> new CookingCraftingModule(ModuleJobs.FARMERS_COOK.get()), //
			() -> CookingCraftingModuleView::new);//

	private ModuleBuildingModules()
	{

	}

}
