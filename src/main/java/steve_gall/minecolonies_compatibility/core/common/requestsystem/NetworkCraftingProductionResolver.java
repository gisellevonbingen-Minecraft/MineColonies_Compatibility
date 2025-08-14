package steve_gall.minecolonies_compatibility.core.common.requestsystem;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.reflect.TypeToken;
import com.minecolonies.api.colony.requestsystem.location.ILocation;
import com.minecolonies.api.colony.requestsystem.manager.IRequestManager;
import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.colony.requestsystem.requester.IRequester;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.util.constant.RSConstants;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.requestsystem.resolvers.core.AbstractBuildingDependentRequestResolver;

import net.minecraft.nbt.CompoundTag;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.ICustomizableRequestable;

public class NetworkCraftingProductionResolver extends AbstractBuildingDependentRequestResolver<ICustomizableRequestable>
{
	private static final TypeToken<ICustomizableRequestable> REQUEST_TYPE = TypeToken.of(ICustomizableRequestable.class);

	public static NetworkCraftingProductionResolver deserialize(ILocation location, IToken<?> token, CompoundTag compound)
	{
		return new NetworkCraftingProductionResolver(location, token);
	}

	public static void serialize(NetworkCraftingProductionResolver resolver, CompoundTag compound)
	{

	}

	public NetworkCraftingProductionResolver(@NotNull ILocation location, @NotNull IToken<?> token)
	{
		super(location, token);
	}

	@Override
	public TypeToken<? extends ICustomizableRequestable> getRequestType()
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
	public boolean canResolveRequest(@NotNull IRequestManager manager, IRequest<? extends ICustomizableRequestable> request)
	{
		if (manager.getColony().getWorld().isClientSide())
		{
			return false;
		}

		return request.getRequest().getObject() instanceof NetworkCrafting && request.getRequester().getLocation().equals(this.getLocation());
	}

	@Override
	public int getPriority()
	{
		return RSConstants.CONST_DEFAULT_RESOLVER_PRIORITY;
	}

	@Override
	public boolean canResolveForBuilding(@NotNull IRequestManager manager, @NotNull IRequest<? extends ICustomizableRequestable> request, @NotNull AbstractBuilding building)
	{
		return true;
	}

	@Override
	public void onAssignedRequestBeingCancelled(@NotNull IRequestManager manager, @NotNull IRequest<? extends ICustomizableRequestable> request)
	{

	}

	@Override
	public void onAssignedRequestCancelled(@NotNull IRequestManager manager, @NotNull IRequest<? extends ICustomizableRequestable> request)
	{

	}

	@Override
	public void onRequestedRequestComplete(@NotNull IRequestManager manager, @NotNull IRequest<?> request)
	{

	}

	@Override
	public void onRequestedRequestCancelled(@NotNull IRequestManager manager, @NotNull IRequest<?> request)
	{

	}

	@Override
	public @Nullable List<IToken<?>> attemptResolveForBuilding(@NotNull IRequestManager manager, @NotNull IRequest<? extends ICustomizableRequestable> request, @NotNull AbstractBuilding building)
	{
		if (manager.getColony().getWorld().isClientSide())
		{
			return null;
		}

		return Collections.emptyList();
	}

	@Override
	public void resolveForBuilding(@NotNull IRequestManager manager, @NotNull IRequest<? extends ICustomizableRequestable> request, @NotNull AbstractBuilding building)
	{

	}

	@Override
	public void onColonyUpdate(@NotNull IRequestManager manager, @NotNull Predicate<IRequest<?>> shouldTriggerReassign)
	{
		super.onColonyUpdate(manager, shouldTriggerReassign);
	}

}
