package steve_gall.minecolonies_compatibility.api.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class GhostSlot extends Slot implements IItemGhostSlot
{
	public GhostSlot(Container container, int slot, int x, int y)
	{
		super(container, slot, x, y);
	}

}
