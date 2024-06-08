package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import com.refinedmods.refinedstorage.container.BaseContainerMenu;

import net.minecraft.world.entity.player.Player;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.init.ModuleMenuTypes;

public class CitizenGridContainerMenu extends BaseContainerMenu
{
	public CitizenGridContainerMenu(CitizenGridBlockEntity grid, Player player, int windowId)
	{
		super(ModuleMenuTypes.CITIZEN_GRID.get(), grid, player, windowId);

		this.addPlayerInventory(8, 55);
	}

}
