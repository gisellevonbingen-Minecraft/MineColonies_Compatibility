package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;
import com.minecolonies.api.colony.buildings.modules.IAssignmentModuleView;
import com.minecolonies.core.colony.buildings.moduleviews.WorkerBuildingModuleView;

@Mixin(value = WorkerBuildingModuleView.class, remap = false)
public abstract class WorkerBuildingModuleViewMixin extends AbstractBuildingModuleView implements IAssignmentModuleView
{
	@Inject(method = "isFull", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void isFull(CallbackInfoReturnable<Boolean> cir)
	{
		if (this.buildingView.getBuildingType() == ModBuildings.lumberjack.get())
		{
			cir.setReturnValue(this.buildingView.getAllAssignedCitizens().size() >= this.getMaxInhabitants());
		}

	}

}
