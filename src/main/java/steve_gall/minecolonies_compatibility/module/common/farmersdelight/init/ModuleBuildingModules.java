package steve_gall.minecolonies_compatibility.module.common.farmersdelight.init;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.api.equipment.ModEquipmentTypes;

import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.CookingCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.CookingCraftingModuleView;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.CuttingCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.CuttingCraftingModuleView;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.PlatingCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module.PlatingCraftingModuleView;

public class ModuleBuildingModules
{
	public static final BuildingEntry.ModuleProducer<CuttingCraftingModule, CuttingCraftingModuleView> CHEF_CUTTING = new BuildingEntry.ModuleProducer<>("chef_farmers_cutting", //
			() -> new CuttingCraftingModule(ModJobs.chef.get(), ModToolTypes.KNIFE.getToolType()), //
			() -> CuttingCraftingModuleView::new);//

	public static final BuildingEntry.ModuleProducer<CookingCraftingModule, CookingCraftingModuleView> CHEF_COOKING = new BuildingEntry.ModuleProducer<>("chef_farmers_cooking", //
			() -> new CookingCraftingModule(ModJobs.chef.get()), //
			() -> CookingCraftingModuleView::new);//

	public static final BuildingEntry.ModuleProducer<PlatingCraftingModule, PlatingCraftingModuleView> CHEF_PLATING = new BuildingEntry.ModuleProducer<>("chef_farmers_plating", //
			() -> new PlatingCraftingModule(ModJobs.chef.get()), //
			() -> PlatingCraftingModuleView::new);//

	public static final BuildingEntry.ModuleProducer<CuttingCraftingModule, CuttingCraftingModuleView> LUMBERJACK_CUTTING = new BuildingEntry.ModuleProducer<>("lumberjack_farmers_cutting", //
			() -> new CuttingCraftingModule(ModJobs.lumberjack.get(), ModEquipmentTypes.axe.get()), //
			() -> CuttingCraftingModuleView::new);//

	public static final BuildingEntry.ModuleProducer<CuttingCraftingModule, CuttingCraftingModuleView> STONEMASON_CUTTING = new BuildingEntry.ModuleProducer<>("stonemason_farmers_pickaxe", //
			() -> new CuttingCraftingModule(ModJobs.stoneMason.get(), ModEquipmentTypes.pickaxe.get()), //
			() -> CuttingCraftingModuleView::new);//

	private ModuleBuildingModules()
	{

	}

}
