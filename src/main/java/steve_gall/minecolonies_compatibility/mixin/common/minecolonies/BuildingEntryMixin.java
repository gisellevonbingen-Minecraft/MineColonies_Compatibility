package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.buildings.modules.ISettingsModule;
import com.minecolonies.api.colony.buildings.modules.settings.ISetting;
import com.minecolonies.api.colony.buildings.modules.settings.ISettingKey;
import com.minecolonies.api.colony.buildings.modules.settings.ISettingsModuleView;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry.ModuleProducer;
import com.minecolonies.core.colony.buildings.modules.BuildingModules;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;

@Mixin(value = BuildingEntry.class, remap = false)
public abstract class BuildingEntryMixin
{
	@Inject(method = "produceBuilding", remap = false, at = @At(value = "TAIL"), cancellable = true)
	private void produceBuilding(BlockPos position, IColony colony, CallbackInfoReturnable<IBuilding> cir)
	{
		var building = cir.getReturnValue();
		var buildingType = building.getBuildingType();

		if (buildingType == ModBuildings.guardTower.get())
		{
			this.injectSettings(building, BuildingModules.GUARD_SETTINGS, ModBuildingModules.GUARD_SETTINGS);
		}
		else if (buildingType == ModBuildings.barracksTower.get())
		{
			this.injectSettings(building, BuildingModules.GUARD_SETTINGS, ModBuildingModules.GUARD_SETTINGS);
		}
		else if (buildingType == ModBuildings.lumberjack.get())
		{
			this.injectSettings(building, BuildingModules.FORESTER_SETTINGS, ModBuildingModules.ORCHARDIST_SETTINGS);
		}

	}

	private <MODULECLASS extends ISettingsModule, VIEWCLASS extends ISettingsModuleView> void injectSettings(IBuilding building, ModuleProducer<MODULECLASS, VIEWCLASS> producer, List<Pair<ISettingKey<?>, ISetting<?>>> settings)
	{
		var module = building.getModule(producer);

		if (module != null)
		{
			settings.stream().forEach(pair -> module.with(pair.getFirst(), pair.getSecond()));
		}

	}

}
