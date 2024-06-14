package steve_gall.minecolonies_compatibility.api.common.building.module;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	@Nullable
	NetworkStorageModule getLinkedModule();

	@Nullable
	NetworkStorageModuleView getLinkedModuleView();

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
}
