package steve_gall.minecolonies_compatibility.module.common.create;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class AddressMessage extends AbstractMessage
{
	private final BlockPos position;
	private final String address;

	public AddressMessage(CitizenStockKeeperBlockEntity blockEntity, String address)
	{
		this.position = blockEntity.getBlockPos();
		this.address = address;
	}

	public AddressMessage(FriendlyByteBuf buffer)
	{
		this.position = buffer.readBlockPos();
		this.address = buffer.readUtf();
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeBlockPos(this.position);
		buffer.writeUtf(this.address);
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		super.handle(context);

		var player = context.getSender();

		if (player == null)
		{
			return;
		}

		if (player.level().getBlockEntity(this.position) instanceof CitizenStockKeeperBlockEntity blockEntity)
		{
			blockEntity.setAddress(this.address);
		}

	}

	public BlockPos getPosition()
	{
		return this.position;
	}

	public String getAddress()
	{
		return this.address;
	}

}
