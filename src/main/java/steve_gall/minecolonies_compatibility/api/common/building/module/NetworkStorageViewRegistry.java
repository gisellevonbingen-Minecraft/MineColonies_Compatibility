package steve_gall.minecolonies_compatibility.api.common.building.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NetworkStorageViewRegistry
{
	private static final List<NetworkStorageViewProvider> REGISTRY = new ArrayList<>();

	public static void register(@NotNull NetworkStorageViewProvider provider)
	{
		REGISTRY.add(provider);
	}

	@Nullable
	public static INetworkStorageView select(@NotNull BlockEntity blockEntity, @Nullable Direction direction)
	{
		return REGISTRY.stream().map(provider ->
		{
			return provider.getNetworkStorageView(blockEntity, direction);
		}).filter(view ->
		{
			return view != null && view.getDirection() == direction;
		}).findFirst().orElse(null);
	}

	public static List<NetworkStorageViewProvider> getRegistry()
	{
		return Collections.unmodifiableList(REGISTRY);
	}

	public interface NetworkStorageViewProvider
	{
		@Nullable
		INetworkStorageView getNetworkStorageView(@NotNull BlockEntity blockEntity, @Nullable Direction direction);
	}

	private NetworkStorageViewRegistry()
	{

	}

}
