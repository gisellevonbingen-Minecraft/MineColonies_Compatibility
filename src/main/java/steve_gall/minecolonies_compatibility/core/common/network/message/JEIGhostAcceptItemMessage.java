package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;
import steve_gall.minecolonies_compatibility.api.common.inventory.IItemGhostMenu;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class JEIGhostAcceptItemMessage extends AbstractMessage
{
	private final int slotNumber;
	private final ItemStack stack;
	private final boolean isVirtual;

	public JEIGhostAcceptItemMessage(int slotNumber, ItemStack stack, boolean isVirtual)
	{
		this.slotNumber = slotNumber;
		this.stack = stack;
		this.isVirtual = isVirtual;
	}

	public JEIGhostAcceptItemMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.slotNumber = buffer.readInt();
		this.stack = buffer.readItem();
		this.isVirtual = buffer.readBoolean();
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeInt(this.slotNumber);
		buffer.writeItem(this.stack);
		buffer.writeBoolean(this.isVirtual);
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

		if (player.containerMenu instanceof IItemGhostMenu ghostMenu)
		{
			ghostMenu.onGhostAcceptItem(this.slotNumber, this.stack, this.isVirtual);
		}

	}

	public boolean isVirtual()
	{
		return this.isVirtual;
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
