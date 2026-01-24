package steve_gall.minecolonies_compatibility.core.common.requestsystem;

import com.google.common.reflect.TypeToken;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.colony.requestsystem.location.ILocation;
import com.minecolonies.api.colony.requestsystem.resolver.IRequestResolverFactory;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.util.constant.TypeConstants;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import steve_gall.minecolonies_compatibility.api.common.SerializationIds;

public class NetworkCraftingRequestResolverFactory implements IRequestResolverFactory<NetworkCraftingRequestResolver>
{
	private static final String NBT_TOKEN = "Token";
	private static final String NBT_LOCATION = "Location";

	@Override
	public TypeToken<? extends NetworkCraftingRequestResolver> getFactoryOutputType()
	{
		return TypeToken.of(NetworkCraftingRequestResolver.class);
	}

	@Override
	public TypeToken<? extends ILocation> getFactoryInputType()
	{
		return TypeConstants.ILOCATION;
	}

	@Override
	public NetworkCraftingRequestResolver getNewInstance(IFactoryController factoryController, ILocation location, Object... context)
	{
		return new NetworkCraftingRequestResolver(location, factoryController.getNewInstance(TypeConstants.ITOKEN));
	}

	@Override
	public CompoundTag serialize(HolderLookup.Provider provider, IFactoryController controller, NetworkCraftingRequestResolver input)
	{
		var compound = new CompoundTag();
		compound.put(NBT_TOKEN, controller.serializeTag(provider, input.getId()));
		compound.put(NBT_LOCATION, controller.serializeTag(provider, input.getLocation()));

		return compound;
	}

	@Override
	public NetworkCraftingRequestResolver deserialize(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag)
	{
		IToken<?> token = controller.deserializeTag(provider, tag.getCompound(NBT_TOKEN));
		ILocation location = controller.deserializeTag(provider, tag.getCompound(NBT_LOCATION));
		return new NetworkCraftingRequestResolver(location, token);
	}

	@Override
	public void serialize(IFactoryController controller, NetworkCraftingRequestResolver input, RegistryFriendlyByteBuf buffer)
	{
		controller.serialize(buffer, input.getId());
		controller.serialize(buffer, input.getLocation());
	}

	@Override
	public NetworkCraftingRequestResolver deserialize(IFactoryController controller, RegistryFriendlyByteBuf buffer) throws Throwable
	{
		IToken<?> token = controller.deserialize(buffer);
		ILocation location = controller.deserialize(buffer);
		return new NetworkCraftingRequestResolver(location, token);
	}

	@Override
	public short getSerializationId()
	{
		return SerializationIds.NETWORK_CRAFTING_REQUEST_RESOLVER;
	}

}
