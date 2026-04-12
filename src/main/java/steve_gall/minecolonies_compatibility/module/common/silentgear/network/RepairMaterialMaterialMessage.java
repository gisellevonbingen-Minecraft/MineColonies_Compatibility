package steve_gall.minecolonies_compatibility.module.common.silentgear.network;

import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;
import com.minecolonies.api.util.SoundUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import steve_gall.minecolonies_compatibility.core.common.network.message.BuildingModuleMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.building.module.RepairMaterialListModule;

public class RepairMaterialMaterialMessage extends BuildingModuleMessage
{
	public static RepairMaterialMaterialMessage add(IBuildingModuleView module, ItemStack item)
	{
		return new RepairMaterialMaterialMessage(module, true, item);
	}

	public static RepairMaterialMaterialMessage remove(IBuildingModuleView module, ItemStack item)
	{
		return new RepairMaterialMaterialMessage(module, false, item);
	}

	private final boolean add;
	private final ItemStack item;

	public RepairMaterialMaterialMessage(IBuildingModuleView module, boolean add, ItemStack item)
	{
		super(module);

		this.add = add;
		this.item = item.copy();
	}

	public RepairMaterialMaterialMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.add = buffer.readBoolean();
		this.item = buffer.readItem();
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeBoolean(this.add);
		buffer.writeItem(this.item);
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		super.handle(context);

		var player = context.getSender();

		if (this.getModule() instanceof RepairMaterialListModule module)
		{
			var result = false;

			if (this.add)
			{
				result = module.addRepairMaterial(this.item);
			}
			else
			{
				result = module.removeRepairMaterial(this.item);
			}

			if (result)
			{
				SoundUtils.playSuccessSound(player, player.blockPosition());
			}
			else
			{
				SoundUtils.playErrorSound(player, player.blockPosition());
			}

		}

	}

	public boolean isAdd()
	{
		return this.add;
	}

	public ItemStack getItem()
	{
		return this.item.copy();
	}

}
