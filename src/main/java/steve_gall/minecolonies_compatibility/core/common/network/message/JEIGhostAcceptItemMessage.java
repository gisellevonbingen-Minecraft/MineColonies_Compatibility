package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.api.common.inventory.IItemGhostMenu;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class JEIGhostAcceptItemMessage extends AbstractMessage
{
	public static final CustomPacketPayload.Type<JEIGhostAcceptItemMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("jei_ghost_accept_item"));

	private final int slotNumber;
	private final ItemStack stack;
	private final boolean isVirtual;

	public JEIGhostAcceptItemMessage(int slotNumber, ItemStack stack, boolean isVirtual)
	{
		this.slotNumber = slotNumber;
		this.stack = stack;
		this.isVirtual = isVirtual;
	}

	public JEIGhostAcceptItemMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.slotNumber = buffer.readInt();
		this.stack = ItemSerializationHelper.deserialize(buffer);
		this.isVirtual = buffer.readBoolean();
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeInt(this.slotNumber);
		ItemSerializationHelper.serialize(buffer, this.stack);
		buffer.writeBoolean(this.isVirtual);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		var player = context.player();

		if (player.containerMenu instanceof IItemGhostMenu ghostMenu)
		{
			ghostMenu.onGhostAcceptItem(this.slotNumber, this.stack, this.isVirtual);
		}

	}

	@Override
	public CustomPacketPayload.Type<JEIGhostAcceptItemMessage> type()
	{
		return TYPE;
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
