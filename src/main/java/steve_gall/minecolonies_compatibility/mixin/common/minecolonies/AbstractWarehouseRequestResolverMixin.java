package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.core.colony.requestsystem.resolvers.core.AbstractWarehouseRequestResolver;
import com.minecolonies.core.tileentities.TileEntityWareHouse;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;

@Mixin(value = AbstractWarehouseRequestResolver.class, remap = false)
public abstract class AbstractWarehouseRequestResolverMixin
{
	@WrapOperation(method = "attemptResolveRequest", remap = false, at = @At(value = "INVOKE", target = "getMatchingItemStacksInWarehouse", remap = false))
	private List<Tuple<ItemStack, BlockPos>> attemptResolveRequest_getMatchingItemStacksInWarehouse(TileEntityWareHouse wareHouse, Predicate<ItemStack> itemStackSelectionPredicate, Operation<List<Tuple<ItemStack, BlockPos>>> operation, @Local IRequest<? extends IDeliverable> request)
	{
		var list = new ArrayList<>(operation.call(wareHouse, itemStackSelectionPredicate));
		list.addAll(this.minecolonies_compatibility$getMatchingItemStacksInWarehouse(wareHouse, itemStackSelectionPredicate, request.getRequest().getCount()));
		return list;
	}

	@WrapOperation(method = "getFollowupRequestForCompletion", remap = false, at = @At(value = "INVOKE", target = "getMatchingItemStacksInWarehouse", remap = false))
	private List<Tuple<ItemStack, BlockPos>> getFollowupRequestForCompletion_getMatchingItemStacksInWarehouse(TileEntityWareHouse wareHouse, Predicate<ItemStack> itemStackSelectionPredicate, Operation<List<Tuple<ItemStack, BlockPos>>> operation, @Local IRequest<? extends IDeliverable> request)
	{
		var list = new ArrayList<>(operation.call(wareHouse, itemStackSelectionPredicate));
		list.addAll(this.minecolonies_compatibility$getMatchingItemStacksInWarehouse(wareHouse, itemStackSelectionPredicate, request.getRequest().getCount()));
		return list;
	}

	private List<Tuple<ItemStack, BlockPos>> minecolonies_compatibility$getMatchingItemStacksInWarehouse(TileEntityWareHouse wareHouse, Predicate<ItemStack> itemStackSelectionPredicate, int limit)
	{
		var module = wareHouse.getBuilding().getModule(ModBuildingModules.NETWORK_STORAGE);

		if (module == null)
		{
			return Collections.emptyList();
		}

		return module.getMatchingItemStacks(itemStackSelectionPredicate, limit);
	}

}
