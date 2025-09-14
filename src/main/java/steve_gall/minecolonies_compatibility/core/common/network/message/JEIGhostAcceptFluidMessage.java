package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent.Context;
import steve_gall.minecolonies_compatibility.api.common.inventory.IFluidGhostMenu;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class JEIGhostAcceptFluidMessage extends AbstractMessage
{
	private final int slotNumber;
	private final FluidStack stack;

	public JEIGhostAcceptFluidMessage(int slotNumber, FluidStack stack)
	{
		this.slotNumber = slotNumber;
		this.stack = stack;
	}

	public JEIGhostAcceptFluidMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.slotNumber = buffer.readInt();
		this.stack = buffer.readFluidStack();
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeInt(this.slotNumber);
		buffer.writeFluidStack(this.stack);
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

		if (player.containerMenu instanceof IFluidGhostMenu ghostMenu)
		{
			ghostMenu.onGhostAcceptFluid(this.slotNumber, this.stack);
		}

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
