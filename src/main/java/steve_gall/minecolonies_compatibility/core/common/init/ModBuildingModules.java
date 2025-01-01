package steve_gall.minecolonies_compatibility.core.common.init;

import java.util.Arrays;
import java.util.List;

import com.minecolonies.api.colony.buildings.modules.settings.ISetting;
import com.minecolonies.api.colony.buildings.modules.settings.ISettingKey;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.entity.citizen.Skill;
import com.minecolonies.core.colony.buildings.modules.CraftingWorkerBuildingModule;
import com.minecolonies.core.colony.buildings.modules.GuardBuildingModule;
import com.minecolonies.core.colony.buildings.modules.WorkerBuildingModule;
import com.minecolonies.core.colony.buildings.modules.settings.BoolSetting;
import com.minecolonies.core.colony.buildings.modules.settings.SettingKey;
import com.minecolonies.core.colony.buildings.moduleviews.CombinedHiringLimitModuleView;
import com.minecolonies.core.colony.buildings.moduleviews.WorkerBuildingModuleView;
import com.mojang.datafixers.util.Pair;

import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.building.module.BucketFillingCraftingModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.BucketFillingCraftingModuleView;
import steve_gall.minecolonies_compatibility.core.common.building.module.FruitListModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.FruitListModuleView;
import steve_gall.minecolonies_compatibility.core.common.building.module.LavaCauldronModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModuleView;
import steve_gall.minecolonies_compatibility.core.common.building.module.RestrictableModuleView;
import steve_gall.minecolonies_compatibility.core.common.building.module.SmithingCraftingModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.SmithingCraftingModuleView;
import steve_gall.minecolonies_compatibility.core.common.building.module.SmithingTemplateCraftingModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.SmithingTemplateCraftingModuleView;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.EntityAIWorkOrchardist;

public class ModBuildingModules
{
	public static final ISettingKey<BoolSetting> REQUEST_FIREWORK_ROCKET = new SettingKey<>(BoolSetting.class, MineColoniesCompatibility.rl("request_firework_rocket"));

	public static final List<Pair<ISettingKey<?>, ISetting<?>>> GUARD_SETTINGS = Arrays.asList(//
			Pair.of(REQUEST_FIREWORK_ROCKET, new BoolSetting(false)) //
	);

	public static final BuildingEntry.ModuleProducer<GuardBuildingModule, CombinedHiringLimitModuleView> GUNNER_TOWER_WORK = new BuildingEntry.ModuleProducer<>("gunner_tower_work", //
			() -> new GuardBuildingModule(ModGuardTypes.GUNNER.get(), true, b ->
			{
				var jobEntry = ModJobs.GUNNER.get();
				return CustomizedAI.getValues().stream().filter(e -> e.getJobEntry() == jobEntry).findAny().isPresent() ? 1 : 0;
			}), //
			() -> CombinedHiringLimitModuleView::new);
	public static final BuildingEntry.ModuleProducer<GuardBuildingModule, CombinedHiringLimitModuleView> GUNNER_BARRACKS_WORK = new BuildingEntry.ModuleProducer<>("gunner_barracks_work", //
			() -> new GuardBuildingModule(ModGuardTypes.GUNNER.get(), true, b ->
			{
				var jobEntry = ModJobs.GUNNER.get();
				return CustomizedAI.getValues().stream().filter(e -> e.getJobEntry() == jobEntry).findAny().isPresent() ? b.getBuildingLevel() : 0;
			}), () -> CombinedHiringLimitModuleView::new);

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

	public static final BuildingEntry.ModuleProducer<NetworkStorageModule, NetworkStorageModuleView> NETWORK_STORAGE = new BuildingEntry.ModuleProducer<>("warehouse_refinfedstoprage_grid", //
			() -> new NetworkStorageModule(), //
			() -> NetworkStorageModuleView::new);

	public static final BuildingEntry.ModuleProducer<CraftingWorkerBuildingModule, WorkerBuildingModuleView> FLUID_MANAGER_WORK = new BuildingEntry.ModuleProducer<>("fluid_manager_work", //
			() -> new CraftingWorkerBuildingModule(ModJobs.FLUID_MANAGER.get(), Skill.Focus, Skill.Athletics, false, b -> 1), //
			() -> WorkerBuildingModuleView::new);

	public static final BuildingEntry.ModuleProducer<BucketFillingCraftingModule, BucketFillingCraftingModuleView> FLUID_MANAGER_BUCKET_FILLING = new BuildingEntry.ModuleProducer<>("fluid_manager_bucket_filling", //
			() -> new BucketFillingCraftingModule("fluid_manager_bucket_filling", ModJobs.FLUID_MANAGER.get()), //
			() -> BucketFillingCraftingModuleView::new);

	public static final BuildingEntry.ModuleProducer<LavaCauldronModule, RestrictableModuleView> FLUID_MANAGER_LAVA_CAULDRON = new BuildingEntry.ModuleProducer<>("fluid_manager_lava_cauldron", //
			() -> new LavaCauldronModule()
			{
				@Override
				public int getSearchRange()
				{
					return MineColoniesCompatibilityConfigServer.INSTANCE.jobs.fluidManager.searchRange.get().intValue();
				}
			}, //
			() -> () -> new RestrictableModuleView()
			{
				@Override
				public String getIcon()
				{
					return "fluid_manager_lava_cauldron";
				};
			});

	public static final BuildingEntry.ModuleProducer<SmithingCraftingModule, SmithingCraftingModuleView> BLACKSMITH_SMITHING = new BuildingEntry.ModuleProducer<>("blacksmith_smithing", //
			() -> new SmithingCraftingModule(com.minecolonies.api.colony.jobs.ModJobs.blacksmith.get()), //
			() -> SmithingCraftingModuleView::new);

	public static final BuildingEntry.ModuleProducer<SmithingTemplateCraftingModule, SmithingTemplateCraftingModuleView> BLACKSMITH_SMITHING_TEMPLATE_CRAFTING = new BuildingEntry.ModuleProducer<>("blacksmith_smithing_template_crafting", //
			() -> new SmithingTemplateCraftingModule(com.minecolonies.api.colony.jobs.ModJobs.blacksmith.get()), //
			() -> SmithingTemplateCraftingModuleView::new);
}
