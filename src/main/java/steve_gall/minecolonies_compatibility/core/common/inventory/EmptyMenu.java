package steve_gall.minecolonies_compatibility.core.common.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class EmptyMenu extends AbstractContainerMenu
{
	public static final EmptyMenu INSTANCE = new EmptyMenu();

	public EmptyMenu()
	{
		super(null, 0);
	}

	@Override
	public ItemStack quickMoveStack(Player p_38941_, int p_38942_)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player p_38874_)
	{
		return true;
	}

}
