package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModule;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModuleView;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class RestrictSetAreaMessage extends BuildingModuleMessage
{
	public static final CustomPacketPayload.Type<RestrictSetAreaMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("restrict_set_area"));

	private final BlockPos pos1;
	private final BlockPos pos2;

	public RestrictSetAreaMessage(IRestrictableModuleView module, BlockPos pos1, BlockPos pos2)
	{
		super(module);

		this.pos1 = pos1;
		this.pos2 = pos2;
	}

	public RestrictSetAreaMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.pos1 = buffer.readBlockPos();
		this.pos2 = buffer.readBlockPos();
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeBlockPos(this.pos1);
		buffer.writeBlockPos(this.pos2);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		if (this.getModule() instanceof IRestrictableModule module)
		{
			module.setRestrictArea(this.pos1, this.pos2);
			module.markDirty();
		}

	}

	@Override
	public CustomPacketPayload.Type<RestrictSetAreaMessage> type()
	{
		return TYPE;
	}

	public BlockPos getRestrictAreaPos1()
	{
		return this.pos1;
	}

	public BlockPos getRestrictAreaPos2()
	{
		return this.pos2;
	}

}
