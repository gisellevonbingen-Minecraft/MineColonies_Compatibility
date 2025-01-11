package steve_gall.minecolonies_compatibility.module.common.butchercraft.init;

import java.util.Arrays;
import java.util.List;

import com.minecolonies.api.colony.buildings.modules.settings.ISetting;
import com.minecolonies.api.colony.buildings.modules.settings.ISettingKey;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.core.colony.buildings.modules.settings.BoolSetting;
import com.minecolonies.core.colony.buildings.modules.settings.SettingKey;
import com.mojang.datafixers.util.Pair;

import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.building.module.GrinderCraftingModule;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.building.module.GrinderCraftingModuleView;

public class ModuleBuildingModules
{
	public static final ISettingKey<BoolSetting> SKIP_BLOOD = new SettingKey<>(BoolSetting.class, MineColoniesCompatibility.rl("butchercraft_skip_blood"));

	public static final List<Pair<ISettingKey<?>, ISetting<?>>> HERDER_SETTINGS = Arrays.asList(//
			Pair.of(SKIP_BLOOD, new BoolSetting(true)) //
	);

	public static final BuildingEntry.ModuleProducer<GrinderCraftingModule, GrinderCraftingModuleView> COOKASSISTANT_GRINDER = new BuildingEntry.ModuleProducer<>("cookassistant_butchercraft_grinder", //
			() -> new GrinderCraftingModule(ModJobs.cookassistant.get()), //
			() -> GrinderCraftingModuleView::new);//

	private ModuleBuildingModules()
	{

	}

}
