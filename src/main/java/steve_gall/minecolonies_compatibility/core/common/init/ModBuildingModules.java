package steve_gall.minecolonies_compatibility.core.common.init;

import java.util.Arrays;
import java.util.List;

import com.minecolonies.api.colony.buildings.modules.settings.ISetting;
import com.minecolonies.api.colony.buildings.modules.settings.ISettingKey;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry.ModuleProducer;
import com.minecolonies.api.entity.citizen.Skill;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;
import com.minecolonies.core.colony.buildings.modules.BuildingModules;
import com.minecolonies.core.colony.buildings.modules.CraftingWorkerBuildingModule;
import com.minecolonies.core.colony.buildings.modules.GuardBuildingModule;
import com.minecolonies.core.colony.buildings.modules.settings.BoolSetting;
import com.minecolonies.core.colony.buildings.moduleviews.CombinedHiringLimitModuleView;
import com.minecolonies.core.colony.buildings.moduleviews.WorkerBuildingModuleView;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingLumberjack;
import com.mojang.datafixers.util.Pair;

import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.EntityAIWorkOrchardist;

public class ModBuildingModules
{
	public static final BuildingEntry.ModuleProducer<GuardBuildingModule, CombinedHiringLimitModuleView> GUNNER_TOWER_WORK = new BuildingEntry.ModuleProducer<>("gunner_tower_work", () -> new GuardBuildingModule(ModGuardTypes.GUNNER.get(), true, b -> 1), () -> CombinedHiringLimitModuleView::new);

	public static final BuildingEntry.ModuleProducer<CraftingWorkerBuildingModule, WorkerBuildingModuleView> ORCHARDIST_WORK = new BuildingEntry.ModuleProducer<>("orchardist_work", () -> new CraftingWorkerBuildingModule(ModJobs.ORCHARDIST.get(), Skill.Stamina, Skill.Focus, false, b -> 1), () -> WorkerBuildingModuleView::new);

	public static final List<Pair<ISettingKey<?>, ISetting<?>>> ORCHARDIST_SETTINGS = Arrays.asList(//
			Pair.of(EntityAIWorkOrchardist.FERTILIZE, new BoolSetting(true))//
	);
	public static final List<ModuleProducer<?, ?>> ORCHARDIST_BAN_MODULES = Arrays.asList(//
			BuildingModules.FORESTER_CRAFT, //
			BuildingModules.ITEMLIST_SAPLING, //
			BuildingModules.CRAFT_TASK_VIEW//
	);
	public static final List<ISettingKey<?>> ORCHARDIST_BAN_SETTINGS = Arrays.asList(//
			BuildingLumberjack.REPLANT, //
			BuildingLumberjack.DEFOLIATE, //
			AbstractCraftingBuildingModule.RECIPE_MODE, //
			BuildingLumberjack.DYNAMIC_TREES_SIZE, //
			AbstractBuilding.USE_SHEARS//
	);
}
