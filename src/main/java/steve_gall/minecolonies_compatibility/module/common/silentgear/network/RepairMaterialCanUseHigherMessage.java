package steve_gall.minecolonies_compatibility.module.common.silentgear.network;

import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import steve_gall.minecolonies_compatibility.core.common.network.message.BuildingModuleMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.building.module.RepairMaterialListModule;

public class RepairMaterialCanUseHigherMessage extends BuildingModuleMessage
{
	private final boolean canUseHigher;

	public RepairMaterialCanUseHigherMessage(IBuildingModuleView module, boolean add)
	{
		super(module);

		this.canUseHigher = add;
	}

	public RepairMaterialCanUseHigherMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.canUseHigher = buffer.readBoolean();
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeBoolean(this.canUseHigher);
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		super.handle(context);

		if (this.getModule() instanceof RepairMaterialListModule module)
		{
			module.setCanUseHigher(this.canUseHigher);
		}

	}

	public boolean canUseHigher()
	{
		return this.canUseHigher;
	}

}
