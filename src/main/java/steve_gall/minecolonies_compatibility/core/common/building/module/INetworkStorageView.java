package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface INetworkStorageView
{
	void link(NetworkStorageModule module);

	void unlink();

	BlockPos getPos();

	NetworkStorageModule getPairedModule();

	boolean isActive();

	boolean canExtract();

	boolean canInsert();

	List<ItemStack> getMatchingStacks(Predicate<ItemStack> predicate);

	boolean extract(IItemHandlerModifiable to, ItemStack stack);

	ItemStack insert(IItemHandlerModifiable from, ItemStack stack);
}
