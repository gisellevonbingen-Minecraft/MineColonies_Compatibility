package steve_gall.minecolonies_compatibility.api.common.building.module;

import java.util.Optional;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.colony.requestsystem.token.IToken;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModuleView;

public interface INetworkStorageView
{
	@NotNull
	Level getLevel();

	@NotNull
	BlockPos getPos();

	@Nullable
	Direction getDirection();

	@NotNull
	ItemStack getIcon();

	boolean isActive();

	void link(@NotNull NetworkStorageModule module);

	void unlink();

	@NotNull
	Optional<BlockPos> getLinkedPos();

	@Nullable
	NetworkStorageModule getLinkedModule();

	@Nullable
	NetworkStorageModuleView getLinkedModuleView();

	@NotNull
	default NetworkCraftingDestination getNetworkCraftingDestination()
	{
		return NetworkCraftingDestination.VIEW;
	}

	default boolean canRequest()
	{
		var destination = this.getNetworkCraftingDestination();
		return (destination == NetworkCraftingDestination.VIEW && this.canExtract()) || destination == NetworkCraftingDestination.BUILDING;
	}

	boolean canExtract();

	boolean canInsert();

	@NotNull
	Stream<ItemStack> getAllStacks();

	/**
	 *
	 * @param stack
	 * @param simulate
	 * @return extracted stack
	 */
	@NotNull
	ItemStack extractItem(@NotNull ItemStack stack, boolean simulate);

	/**
	 *
	 * @param stack
	 * @param simulate
	 * @return remained stack
	 */
	@NotNull
	ItemStack insertItem(@NotNull ItemStack stack, boolean simulate);

	@NotNull
	public default ItemStack calculateAutocrafting(@NotNull IDeliverable deliverable)
	{
		return ItemStack.EMPTY;
	}

	public default void cancelAutocrafting(@NotNull IToken<?> requestId)
	{

	}

	public default void createAutocrafting(@NotNull IToken<?> requestId)
	{

	}

	public default void updateAutocraftings()
	{

	}

}
