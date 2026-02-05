package steve_gall.minecolonies_compatibility.api.common.inventory;

import net.minecraft.world.item.ItemStack;

public interface IItemGhostSlot
{
	default boolean canAccept(ItemStack item)
	{
		return true;
	}

}
