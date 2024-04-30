package steve_gall.minecolonies_compatibility.core.common.mixin.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IAssignsCitizen;
import com.minecolonies.core.colony.buildings.modules.AbstractAssignedCitizenModule;

@Mixin(value = AbstractAssignedCitizenModule.class, remap = false)
public abstract class AbstractAssignedCitizenModuleMixin extends AbstractBuildingModule implements IAssignsCitizen
{
	@Inject(method = "isFull", at = @At(value = "HEAD"), cancellable = true)
	private void isFull(CallbackInfoReturnable<Boolean> cir)
	{
		if (this.building.getBuildingType() == ModBuildings.lumberjack.get())
		{
			cir.setReturnValue(this.building.getAllAssignedCitizen().size() >= this.getModuleMax());
		}

	}

}
