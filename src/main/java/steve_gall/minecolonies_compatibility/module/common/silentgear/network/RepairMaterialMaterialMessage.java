package steve_gall.minecolonies_compatibility.module.common.silentgear.network;

import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;
import com.minecolonies.api.util.SoundUtils;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.BuildingModuleMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.building.module.RepairMaterialListModule;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class RepairMaterialMaterialMessage extends BuildingModuleMessage
{
	public static final CustomPacketPayload.Type<RepairMaterialMaterialMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("silentgear_repair_material_material"));

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

	public RepairMaterialMaterialMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.add = buffer.readBoolean();
		this.item = ItemSerializationHelper.deserialize(buffer);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeBoolean(this.add);
		ItemSerializationHelper.serialize(buffer, this.item);
	}

	@Override
	public CustomPacketPayload.Type<RepairMaterialMaterialMessage> type()
	{
		return TYPE;
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		var player = context.player();

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
