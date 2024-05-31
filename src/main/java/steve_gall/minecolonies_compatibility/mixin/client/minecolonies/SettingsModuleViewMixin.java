package steve_gall.minecolonies_compatibility.mixin.client.minecolonies;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;
import com.minecolonies.api.colony.buildings.modules.settings.ISetting;
import com.minecolonies.api.colony.buildings.modules.settings.ISettingKey;
import com.minecolonies.core.colony.buildings.moduleviews.SettingsModuleView;
import com.mojang.datafixers.util.Pair;

import steve_gall.minecolonies_compatibility.core.common.building.BuildingViewHelper;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;
import steve_gall.minecolonies_compatibility.core.common.init.ModJobs;

@Mixin(value = SettingsModuleView.class, remap = false)
public abstract class SettingsModuleViewMixin extends AbstractBuildingModuleView
{
	@SuppressWarnings("rawtypes")
	@Inject(method = "getSettingsToShow", remap = false, at = @At(value = "RETURN"), cancellable = true)
	private void getSettingsToShow(CallbackInfoReturnable<List<ISettingKey<? extends ISetting>>> cir)
	{
		var buildingType = this.buildingView.getBuildingType();

		if (buildingType == ModBuildings.guardTower.get())
		{
			var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.ranger;

			if (config.canUseCrossbow.get().booleanValue() && config.canShootFireworkRocket.get().booleanValue())
			{

			}
			else
			{
				var list = new ArrayList<>(cir.getReturnValue());
				list.remove(ModBuildingModules.REQUEST_FIREWORK_ROCKET);
				cir.setReturnValue(list);
			}

		}
		else if (buildingType == ModBuildings.lumberjack.get())
		{
			var anyOrchardist = !BuildingViewHelper.getAssignedCitizens(this.buildingView, ModJobs.ORCHARDIST.get()).isEmpty();
			var list = new ArrayList<>(cir.getReturnValue());

			if (anyOrchardist)
			{
				ModBuildingModules.ORCHARDIST_BAN_SETTINGS.stream().forEach(list::remove);
			}
			else
			{
				ModBuildingModules.ORCHARDIST_SETTINGS.stream().map(Pair::getFirst).forEach(list::remove);
			}

			cir.setReturnValue(list);
		}

	}

}
