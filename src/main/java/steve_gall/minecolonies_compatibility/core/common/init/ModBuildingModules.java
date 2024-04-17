package steve_gall.minecolonies_compatibility.core.common.init;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.core.colony.buildings.modules.GuardBuildingModule;
import com.minecolonies.core.colony.buildings.moduleviews.CombinedHiringLimitModuleView;

public class ModBuildingModules
{
	public static final BuildingEntry.ModuleProducer<GuardBuildingModule, CombinedHiringLimitModuleView> GUNNER_TOWER_WORK = new BuildingEntry.ModuleProducer<>("gunner_tower_work", () -> new GuardBuildingModule(ModGuardTypes.GUNNER.get(), true, b -> 1), () -> CombinedHiringLimitModuleView::new);
}
