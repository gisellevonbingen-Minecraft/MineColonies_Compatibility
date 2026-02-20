package steve_gall.minecolonies_compatibility.module.common.create;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class AddressMessage extends AbstractMessage
{
	public static final CustomPacketPayload.Type<AddressMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("create_address"));

	private final BlockPos position;
	private final String address;

	public AddressMessage(CitizenStockKeeperBlockEntity blockEntity, String address)
	{
		this.position = blockEntity.getBlockPos();
		this.address = address;
	}

	public AddressMessage(RegistryFriendlyByteBuf buffer)
	{
		this.position = buffer.readBlockPos();
		this.address = buffer.readUtf();
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeBlockPos(this.position);
		buffer.writeUtf(this.address);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		var player = context.player();

		if (player.level().getBlockEntity(this.position) instanceof CitizenStockKeeperBlockEntity blockEntity)
		{
			blockEntity.setAddress(this.address);
		}

	}

	@Override
	public CustomPacketPayload.Type<AddressMessage> type()
	{
		return TYPE;
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
