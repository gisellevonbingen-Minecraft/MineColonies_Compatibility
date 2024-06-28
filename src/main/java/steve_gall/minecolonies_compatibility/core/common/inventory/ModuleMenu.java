package steve_gall.minecolonies_compatibility.core.common.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public abstract class ModuleMenu extends BaseMenu
{
	protected final BlockPos buildingId;
	protected final int moduleId;

	protected ModuleMenu(MenuType<?> menuType, int windowId, Inventory inventory, BlockPos buildingId, int moduleId)
	{
		super(menuType, windowId, inventory);
		this.buildingId = buildingId;
		this.moduleId = moduleId;
	}

	protected ModuleMenu(MenuType<?> menuType, int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(menuType, windowId, inventory);
		this.buildingId = buffer.readBlockPos();
		this.moduleId = buffer.readInt();
	}

	public BlockPos getBuildingId()
	{
		return this.buildingId;
	}

	public int getModuleId()
	{
		return this.moduleId;
	}

}
