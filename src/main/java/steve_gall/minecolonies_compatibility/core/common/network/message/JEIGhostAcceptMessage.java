package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;
import steve_gall.minecolonies_compatibility.api.common.inventory.IItemGhostMenu;
import steve_gall.minecolonies_compatibility.core.common.network.AbstractMessage;

public class JEIGhostAcceptMessage extends AbstractMessage
{
	private final int slotNumber;
	private final ItemStack stack;

	public JEIGhostAcceptMessage(int slotNumber, ItemStack stack)
	{
		this.slotNumber = slotNumber;
		this.stack = stack;
	}

	public JEIGhostAcceptMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.slotNumber = buffer.readInt();
		this.stack = buffer.readItem();
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeInt(this.slotNumber);
		buffer.writeItem(this.stack);
	}

	@Override
	public void handle(Context context)
	{
		super.handle(context);

		var player = context.getSender();

		if (player == null)
		{
			return;
		}

		var menu = player.containerMenu;

		if (menu instanceof IItemGhostMenu ghostMenu)
		{
			var slot = menu.getSlot(this.slotNumber);
			ghostMenu.onGhostAccept(slot, this.stack);
		}

	}

	public int getSlotNumber()
	{
		return this.slotNumber;
	}

	public ItemStack getStack()
	{
		return this.stack;
	}

}
