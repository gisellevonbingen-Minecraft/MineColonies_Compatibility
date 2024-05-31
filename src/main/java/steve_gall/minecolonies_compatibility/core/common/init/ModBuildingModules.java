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
import com.minecolonies.core.colony.buildings.modules.GuardBuildingModule;
import com.minecolonies.core.colony.buildings.modules.WorkerBuildingModule;
import com.minecolonies.core.colony.buildings.modules.settings.BoolSetting;
import com.minecolonies.core.colony.buildings.modules.settings.SettingKey;
import com.minecolonies.core.colony.buildings.moduleviews.CombinedHiringLimitModuleView;
import com.minecolonies.core.colony.buildings.moduleviews.WorkerBuildingModuleView;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingLumberjack;
import com.mojang.datafixers.util.Pair;

import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.building.module.FruitListModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.FruitListModuleView;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.EntityAIWorkOrchardist;

public class ModBuildingModules
{
	public static final ISettingKey<BoolSetting> REQUEST_FIREWORK_ROCKET = new SettingKey<>(BoolSetting.class, MineColoniesCompatibility.rl("request_firework_rocket"));

	public static final List<Pair<ISettingKey<?>, ISetting<?>>> GUARD_SETTINGS = Arrays.asList(//
			Pair.of(REQUEST_FIREWORK_ROCKET, new BoolSetting(false)) //
	);

	public static final BuildingEntry.ModuleProducer<GuardBuildingModule, CombinedHiringLimitModuleView> GUNNER_TOWER_WORK = new BuildingEntry.ModuleProducer<>("gunner_tower_work", //
			() -> new GuardBuildingModule(ModGuardTypes.GUNNER.get(), true, b -> 1), //
			() -> CombinedHiringLimitModuleView::new);

	public static final BuildingEntry.ModuleProducer<WorkerBuildingModule, WorkerBuildingModuleView> ORCHARDIST_WORK = new BuildingEntry.ModuleProducer<>("orchardist_work", //
			() -> new WorkerBuildingModule(ModJobs.ORCHARDIST.get(), Skill.Stamina, Skill.Focus, false, b -> 1), //
			() -> WorkerBuildingModuleView::new);

	public static final List<Pair<ISettingKey<?>, ISetting<?>>> ORCHARDIST_SETTINGS = Arrays.asList(//
			Pair.of(EntityAIWorkOrchardist.FERTILIZE, new BoolSetting(true)), //
			Pair.of(EntityAIWorkOrchardist.NEED_MAX_HARVEST, new BoolSetting(true))//
	);

	public static final BuildingEntry.ModuleProducer<FruitListModule, FruitListModuleView> FRUITLIST_BLACKLIST = new BuildingEntry.ModuleProducer<>("fruitlist_blacklist", //
			() -> new FruitListModule(ModBuildingModules.FRUITLIST_BLACKLIST.key), //
			() -> () -> new FruitListModuleView("fruitlist_blacklist", "com.minecolonies.coremod.gui.workerhuts.fruitlist_blacklist", true, fruit -> true));

	public static final List<ModuleProducer<?, ?>> ORCHARDIST_BAN_MODULES = Arrays.asList(//
			BuildingModules.FORESTER_CRAFT, //
			BuildingModules.ITEMLIST_SAPLING, //
			BuildingModules.CRAFT_TASK_VIEW//
	);

	public static final List<ModuleProducer<?, ?>> ORCHARDIST_ONLY_MODULES = Arrays.asList(//
			FRUITLIST_BLACKLIST//
	);
	public static final List<ISettingKey<?>> ORCHARDIST_BAN_SETTINGS = Arrays.asList(//
			BuildingLumberjack.REPLANT, //
			BuildingLumberjack.DEFOLIATE, //
			AbstractCraftingBuildingModule.RECIPE_MODE, //
			BuildingLumberjack.DYNAMIC_TREES_SIZE, //
			AbstractBuilding.USE_SHEARS//
	);

}
