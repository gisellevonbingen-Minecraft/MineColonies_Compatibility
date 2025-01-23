package steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.init;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.colony.jobs.ModJobs;

import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.building.module.CheeseCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.building.module.CheeseCraftingModuleView;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.building.module.CookingCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.building.module.CookingCraftingModuleView;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.building.module.WoodcuttingCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.building.module.WoodcuttingCraftingModuleView;

public class ModuleBuildingModules
{
	public static final BuildingEntry.ModuleProducer<CheeseCraftingModule, CheeseCraftingModuleView> CHEF_CHEESE = new BuildingEntry.ModuleProducer<>("chef_lets_do_meadow_cheese", //
			() -> new CheeseCraftingModule(ModJobs.chef.get()), //
			() -> CheeseCraftingModuleView::new);//

	public static final BuildingEntry.ModuleProducer<CookingCraftingModule, CookingCraftingModuleView> CHEF_COOKING = new BuildingEntry.ModuleProducer<>("chef_lets_do_meadow_cooking", //
			() -> new CookingCraftingModule(ModJobs.chef.get()), //
			() -> CookingCraftingModuleView::new);//

	public static final BuildingEntry.ModuleProducer<WoodcuttingCraftingModule, WoodcuttingCraftingModuleView> SAWMILL_WOODCUTTING = new BuildingEntry.ModuleProducer<>("sawmill_lets_do_meadow_woodcutting ", //
			() -> new WoodcuttingCraftingModule(ModJobs.sawmill.get()), //
			() -> WoodcuttingCraftingModuleView::new);//

	private ModuleBuildingModules()
	{

	}

}
