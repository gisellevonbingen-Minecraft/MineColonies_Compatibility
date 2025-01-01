package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingWareHouse;
import com.minecolonies.core.colony.requestsystem.resolvers.core.AbstractWarehouseRequestResolver;

import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;

@Mixin(value = AbstractWarehouseRequestResolver.class, remap = false)
public abstract class AbstractWarehouseRequestResolverMixin
{
	@Shadow(remap = false)
	protected abstract int getWarehouseInternalCount(final BuildingWareHouse wareHouse, final IRequest<? extends IDeliverable> requestToCheck);

	@Redirect(method = "canResolveRequest", remap = false, at = @At(value = "INVOKE", target = "getWarehouseInternalCount", remap = false))
	private int canResolveRequest_getWarehouseInternalCount(AbstractWarehouseRequestResolver self, BuildingWareHouse wareHouse, IRequest<? extends IDeliverable> requestToCheck)
	{
		var totalCount = this.getWarehouseInternalCount(wareHouse, requestToCheck);
		var module = wareHouse.getModule(ModBuildingModules.NETWORK_STORAGE);

		if (module != null)
		{
			totalCount += module.getMatchingItemStackCount(itemStack -> requestToCheck.getRequest().matches(itemStack), requestToCheck.getRequest().getCount(), 0);
		}

		return totalCount;
	}

}
