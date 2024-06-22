package steve_gall.minecolonies_compatibility.module.common.ae2;

import appeng.menu.AEBaseMenu;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.module.common.ae2.init.ModuleMenuTypes;

public class CitizenTerminalMenu extends AEBaseMenu
{
	public CitizenTerminalMenu(int windowId, Inventory inventory, CitizenTerminalPart part)
	{
		super(ModuleMenuTypes.CITIZEN_TERMINAL.get(), windowId, inventory, part);

		this.createPlayerInventorySlots(inventory);
	}

}
