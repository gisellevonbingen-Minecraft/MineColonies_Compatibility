package steve_gall.minecolonies_compatibility.mixin.client.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.core.client.gui.AbstractModuleWindow;

import steve_gall.minecolonies_compatibility.core.common.building.BuildingViewHelper;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;
import steve_gall.minecolonies_compatibility.core.common.init.ModJobs;

@Mixin(value = AbstractModuleWindow.class, remap = false)
public abstract class AbstractModuleWindowMixin
{
	@Shadow(remap = false)
	private IBuildingView buildingView;

	@Redirect(method = "<init>", remap = false, at = @At(value = "INVOKE", target = "Lcom/minecolonies/api/colony/buildings/modules/IBuildingModuleView;isPageVisible()Z"))
	public boolean init_isPageVisible(IBuildingModuleView moduleView)
	{
		if (this.buildingView.getBuildingType() == ModBuildings.lumberjack.get())
		{
			var anyOrchardist = !BuildingViewHelper.getAssignedCitizens(this.buildingView, ModJobs.ORCHARDIST.get()).isEmpty();

			if (anyOrchardist)
			{
				if (ModBuildingModules.ORCHARDIST_BAN_MODULES.contains(moduleView.getProducer()))
				{
					return false;
				}

			}

		}

		return moduleView.isPageVisible();
	}

}
