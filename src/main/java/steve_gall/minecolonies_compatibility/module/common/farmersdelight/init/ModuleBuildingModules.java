package steve_gall.minecolonies_compatibility.module.common.farmersdelight.init;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.api.util.constant.ToolType;

import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.CookingCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.CookingCraftingModuleView;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.CuttingCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.CuttingCraftingModuleView;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.PlatingCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.PlatingCraftingModuleView;

public class ModuleBuildingModules
{
	public static final BuildingEntry.ModuleProducer<CuttingCraftingModule, CuttingCraftingModuleView> COOKASSISTENT_CUTTING = new BuildingEntry.ModuleProducer<>("cookassistent_farmers_cutting", //
			() -> new CuttingCraftingModule(ModJobs.cookassistant.get(), ModToolTypes.KNIFE.getToolType()), //
			() -> CuttingCraftingModuleView::new);//

	public static final BuildingEntry.ModuleProducer<CookingCraftingModule, CookingCraftingModuleView> COOKASSISTENT_COOKING = new BuildingEntry.ModuleProducer<>("cookassistent_farmers_cooking", //
			() -> new CookingCraftingModule(ModJobs.cookassistant.get()), //
			() -> CookingCraftingModuleView::new);//

	public static final BuildingEntry.ModuleProducer<PlatingCraftingModule, PlatingCraftingModuleView> COOKASSISTENT_PLATING = new BuildingEntry.ModuleProducer<>("cookassistent_farmers_plating", //
			() -> new PlatingCraftingModule(ModJobs.cookassistant.get()), //
			() -> PlatingCraftingModuleView::new);//

	public static final BuildingEntry.ModuleProducer<CuttingCraftingModule, CuttingCraftingModuleView> LUMBERJACK_CUTTING = new BuildingEntry.ModuleProducer<>("lumberjack_farmers_cutting", //
			() -> new CuttingCraftingModule(ModJobs.lumberjack.get(), ToolType.AXE), //
			() -> CuttingCraftingModuleView::new);//

	public static final BuildingEntry.ModuleProducer<CuttingCraftingModule, CuttingCraftingModuleView> STONEMASON_CUTTING = new BuildingEntry.ModuleProducer<>("stonemason_farmers_pickaxe", //
			() -> new CuttingCraftingModule(ModJobs.stoneMason.get(), ToolType.PICKAXE), //
			() -> CuttingCraftingModuleView::new);//

	private ModuleBuildingModules()
	{

	}

}
