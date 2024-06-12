package steve_gall.minecolonies_compatibility.api.common.building.module;

import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;
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
	List<ItemStack> getMatchingStacks(@NotNull Predicate<ItemStack> predicate);

	boolean extract(@NotNull IItemHandlerModifiable to, @NotNull ItemStack stack);

	@NotNull
	ItemStack insert(@NotNull IItemHandlerModifiable from, @NotNull ItemStack stack);
}
