package steve_gall.minecolonies_compatibility.api.common.inventory;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public interface IItemGhostMenu
{
	void onGhostAccept(Slot slot, ItemStack stack);
}
