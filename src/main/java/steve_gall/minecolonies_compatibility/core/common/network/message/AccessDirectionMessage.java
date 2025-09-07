package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.block.entity.IAccessDirectionHolder;
import steve_gall.minecolonies_compatibility.core.common.building.module.AccessDirection;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class AccessDirectionMessage<BLOCK_ENTITY extends BlockEntity & IAccessDirectionHolder> extends AbstractMessage
{
	public static final CustomPacketPayload.Type<AccessDirectionMessage<?>> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("access_direction"));

	private final BlockPos position;
	private final AccessDirection accessDirection;

	public AccessDirectionMessage(BLOCK_ENTITY blockEntity, AccessDirection accessDirection)
	{
		this.position = blockEntity.getBlockPos();
		this.accessDirection = accessDirection;
	}

	public AccessDirectionMessage(RegistryFriendlyByteBuf buffer)
	{
		this.position = buffer.readBlockPos();
		this.accessDirection = buffer.readEnum(AccessDirection.class);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeBlockPos(this.position);
		buffer.writeEnum(this.accessDirection);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		if (context.player().level().getBlockEntity(this.position) instanceof IAccessDirectionHolder holder)
		{
			holder.setAccessDirection(this.accessDirection);
		}

	}

	@Override
	public CustomPacketPayload.Type<AccessDirectionMessage<?>> type()
	{
		return TYPE;
	}

	public BlockPos getPosition()
	{
		return this.position;
	}

	public AccessDirection getAccessDirection()
	{
		return this.accessDirection;
	}

}
