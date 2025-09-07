package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.api.common.inventory.IFluidGhostMenu;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;
import steve_gall.minecolonies_tweaks.core.common.fluid.FluidSerializationHelper;

public class JEIGhostAcceptFluidMessage extends AbstractMessage
{
	public static final CustomPacketPayload.Type<JEIGhostAcceptFluidMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("jei_ghost_accept_fluid"));

	private final int slotNumber;
	private final FluidStack stack;

	public JEIGhostAcceptFluidMessage(int slotNumber, FluidStack stack)
	{
		this.slotNumber = slotNumber;
		this.stack = stack;
	}

	public JEIGhostAcceptFluidMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.slotNumber = buffer.readInt();
		this.stack = FluidSerializationHelper.deserialize(buffer);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeInt(this.slotNumber);
		FluidSerializationHelper.serialize(buffer, this.stack);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		var player = context.player();

		if (player.containerMenu instanceof IFluidGhostMenu ghostMenu)
		{
			ghostMenu.onGhostAcceptFluid(this.slotNumber, this.stack);
		}

	}

	@Override
	public CustomPacketPayload.Type<JEIGhostAcceptFluidMessage> type()
	{
		return TYPE;
	}

	public int getSlotNumber()
	{
		return this.slotNumber;
	}

	public FluidStack getStack()
	{
		return this.stack;
	}

}
