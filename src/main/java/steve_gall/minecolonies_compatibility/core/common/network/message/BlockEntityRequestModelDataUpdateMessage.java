package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class BlockEntityRequestModelDataUpdateMessage extends AbstractMessage
{
	public static final CustomPacketPayload.Type<BlockEntityRequestModelDataUpdateMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("block_entity_request_model_data_update"));

	private final BlockPos pos;
	private final CompoundTag tag;

	public BlockEntityRequestModelDataUpdateMessage(BlockEntity blockEntity)
	{
		this.pos = blockEntity.getBlockPos();
		this.tag = blockEntity.getUpdateTag(blockEntity.getLevel().registryAccess());
	}

	public BlockEntityRequestModelDataUpdateMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.pos = buffer.readBlockPos();
		this.tag = buffer.readNbt();
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeBlockPos(this.pos);
		buffer.writeNbt(this.tag);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		var blockEntity = context.player().level().getBlockEntity(this.pos);

		if (blockEntity != null)
		{
			blockEntity.handleUpdateTag(this.tag, blockEntity.getLevel().registryAccess());
			blockEntity.requestModelDataUpdate();
		}

	}

	@Override
	public CustomPacketPayload.Type<BlockEntityRequestModelDataUpdateMessage> type()
	{
		return TYPE;
	}

	public BlockPos getPos()
	{
		return this.pos;
	}

}
