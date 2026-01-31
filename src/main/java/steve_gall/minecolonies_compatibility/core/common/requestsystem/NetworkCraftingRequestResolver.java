package steve_gall.minecolonies_compatibility.core.common.requestsystem;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.reflect.TypeToken;
import com.minecolonies.api.colony.requestsystem.location.ILocation;
import com.minecolonies.api.colony.requestsystem.manager.IRequestManager;
import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.colony.requestsystem.requester.IRequester;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.util.constant.RSConstants;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.requestsystem.resolvers.core.AbstractBuildingDependentRequestResolver;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.CustomizableRequestable;

public class NetworkCraftingRequestResolver extends AbstractBuildingDependentRequestResolver<IDeliverable>
{
	private static final TypeToken<IDeliverable> REQUEST_TYPE = TypeToken.of(IDeliverable.class);

	public static NetworkCraftingRequestResolver deserialize(ILocation location, IToken<?> token, CompoundTag compound)
	{
		return new NetworkCraftingRequestResolver(location, token);
	}

	public static void serialize(NetworkCraftingRequestResolver resolver, CompoundTag compound)
	{

	}

	public NetworkCraftingRequestResolver(@NotNull ILocation location, @NotNull IToken<?> token)
	{
		super(location, token);
	}

	@Override
	public TypeToken<? extends IDeliverable> getRequestType()
	{
		return REQUEST_TYPE;
	}

	@Override
	public Optional<IRequester> getBuilding(@NotNull IRequestManager manager, @NotNull IToken<?> token)
	{
		if (!manager.getColony().getWorld().isClientSide())
		{
			return Optional.ofNullable(manager.getColony().getRequesterBuildingForPosition(this.getLocation().getInDimensionLocation()));
		}

		return Optional.empty();
	}

	@Override
	public boolean canResolveRequest(@NotNull IRequestManager manager, IRequest<? extends IDeliverable> request)
	{
		if (manager.getColony().getWorld().isClientSide())
		{
			return false;
		}

		var building = manager.getColony().getServerBuildingManager().getBuilding(this.getLocation().getInDimensionLocation());

		if (building == null)
		{
			return false;
		}

		var module = building.getModule(ModBuildingModules.NETWORK_STORAGE);

		if (module == null)
		{
			return false;
		}

		var deliverable = request.getRequest();

		for (var view : module.getExtractableBlocks().toList())
		{
			var calculated = view.calculateAutocrafting(deliverable);

			if (!calculated.isEmpty())
			{
				return true;
			}

		}

		return false;
	}

	@Override
	public int getPriority()
	{
		return RSConstants.CONST_CRAFTING_RESOLVER_PRIORITY;
	}

	@Override
	public boolean canResolveForBuilding(@NotNull IRequestManager manager, @NotNull IRequest<? extends IDeliverable> request, @NotNull AbstractBuilding building)
	{
		return true;
	}

	@Override
	public void onAssignedRequestBeingCancelled(@NotNull IRequestManager manager, @NotNull IRequest<? extends IDeliverable> request)
	{

	}

	@Override
	public void onAssignedRequestCancelled(@NotNull IRequestManager manager, @NotNull IRequest<? extends IDeliverable> request)
	{

	}

	@Override
	public void onRequestedRequestComplete(@NotNull IRequestManager manager, @NotNull IRequest<?> request)
	{

	}

	@Override
	public void onRequestedRequestCancelled(@NotNull IRequestManager manager, @NotNull IRequest<?> request)
	{
		if (request.getRequest() instanceof CustomizableRequestable cr && cr.getObject() instanceof NetworkCrafting)
		{
			if (manager.getColony().getWorld().isClientSide())
			{
				return;
			}

			var building = manager.getColony().getServerBuildingManager().getBuilding(this.getLocation().getInDimensionLocation());

			if (building == null)
			{
				return;
			}

			var module = building.getModule(ModBuildingModules.NETWORK_STORAGE);

			if (module == null)
			{
				return;
			}

			for (var view : module.getBlocks().toList())
			{
				view.cancelAutocrafting(request.getId());
			}

		}

	}

	@Override
	public @Nullable List<IToken<?>> attemptResolveForBuilding(@NotNull IRequestManager manager, @NotNull IRequest<? extends IDeliverable> request, @NotNull AbstractBuilding building)
	{
		if (manager.getColony().getWorld().isClientSide())
		{
			return null;
		}

		var module = building.getModule(ModBuildingModules.NETWORK_STORAGE);

		if (module == null)
		{
			return null;
		}

		var deliverable = request.getRequest();

		for (var view : module.getExtractableBlocks().toList())
		{
			var calculated = view.calculateAutocrafting(deliverable);

			if (calculated.isEmpty())
			{
				continue;
			}

			var networkCrafting = new NetworkCrafting();
			networkCrafting.setCycles((deliverable.getCount() + calculated.getCount() - 1) / calculated.getCount());
			networkCrafting.setItem(calculated);
			networkCrafting.setView(view.getIcon());
			networkCrafting.setText(Component.literal("READY"));

			var child = manager.createRequest(this, new CustomizableRequestable(networkCrafting));
			view.createAutocrafting(child);
			return Arrays.asList(child);
		}

		return null;
	}

	@Override
	public void resolveForBuilding(@NotNull IRequestManager manager, @NotNull IRequest<? extends IDeliverable> request, @NotNull AbstractBuilding building)
	{
		manager.updateRequestState(request.getId(), RequestState.RESOLVED);
	}

	@Override
	public @NotNull MutableComponent getRequesterDisplayName(@NotNull IRequestManager manager, @NotNull IRequest<?> request)
	{
		if (request.getRequest() instanceof CustomizableRequestable cr && cr.getObject() instanceof NetworkCrafting networkCrafting)
		{
			var view = networkCrafting.getView();

			if (!view.isEmpty())
			{
				return view.getHoverName().copy();
			}

		}

		return super.getRequesterDisplayName(manager, request);
	}

}
