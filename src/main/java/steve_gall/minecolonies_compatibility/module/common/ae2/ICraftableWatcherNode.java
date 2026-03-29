package steve_gall.minecolonies_compatibility.module.common.ae2;

import appeng.api.networking.IGridNodeService;
import appeng.api.networking.crafting.ICraftingService;
import appeng.api.stacks.AEKey;

public interface ICraftableWatcherNode extends IGridNodeService
{
	void onCraftableChange(ICraftingService craftingGrid, AEKey what);
}
