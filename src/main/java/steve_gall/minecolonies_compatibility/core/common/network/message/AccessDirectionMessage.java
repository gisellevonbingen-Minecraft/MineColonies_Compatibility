package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import steve_gall.minecolonies_compatibility.core.common.block.entity.IAccessDirectionHolder;
import steve_gall.minecolonies_compatibility.core.common.building.module.AccessDirection;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class AccessDirectionMessage<BLOCK_ENTITY extends BlockEntity & IAccessDirectionHolder> extends AbstractMessage
{
	private final BlockPos position;
	private final AccessDirection accessDirection;

	public AccessDirectionMessage(BLOCK_ENTITY blockEntity, AccessDirection accessDirection)
	{
		this.position = blockEntity.getBlockPos();
		this.accessDirection = accessDirection;
	}

	public AccessDirectionMessage(FriendlyByteBuf buffer)
	{
		this.position = buffer.readBlockPos();
		this.accessDirection = buffer.readEnum(AccessDirection.class);
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeBlockPos(this.position);
		buffer.writeEnum(this.accessDirection);
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

		if (player.level.getBlockEntity(this.position) instanceof IAccessDirectionHolder holder)
		{
			holder.setAccessDirection(this.accessDirection);
		}

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
