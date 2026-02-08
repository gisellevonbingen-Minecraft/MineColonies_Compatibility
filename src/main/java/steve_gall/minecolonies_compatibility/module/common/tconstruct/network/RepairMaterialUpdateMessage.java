package steve_gall.minecolonies_compatibility.module.common.tconstruct.network;

import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;
import com.minecolonies.api.util.MessageUtils;
import com.minecolonies.api.util.SoundUtils;
import com.minecolonies.api.util.constant.TranslationConstants;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import steve_gall.minecolonies_compatibility.core.common.network.message.BuildingModuleMessage;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.building.module.RepairMaterialListModule;

public class RepairMaterialUpdateMessage extends BuildingModuleMessage
{
	public static RepairMaterialUpdateMessage add(IBuildingModuleView module, ItemStack item)
	{
		return new RepairMaterialUpdateMessage(module, true, item, null);
	}

	public static RepairMaterialUpdateMessage remove(IBuildingModuleView module, MaterialId material)
	{
		return new RepairMaterialUpdateMessage(module, false, ItemStack.EMPTY, material);
	}

	private final boolean add;
	private final MaterialId materialId;
	private final ItemStack item;

	public RepairMaterialUpdateMessage(IBuildingModuleView module, boolean add, ItemStack item, MaterialId materialId)
	{
		super(module);

		this.add = add;
		this.item = item.copy();
		this.materialId = materialId;
	}

	public RepairMaterialUpdateMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.add = buffer.readBoolean();

		if (this.add)
		{
			this.item = buffer.readItem();
			this.materialId = null;
		}
		else
		{
			this.item = ItemStack.EMPTY;
			this.materialId = new MaterialId(buffer.readResourceLocation());
		}

	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeBoolean(this.add);

		if (this.add)
		{
			buffer.writeItem(this.item);
		}
		else
		{
			buffer.writeResourceLocation(this.materialId);
		}

	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		super.handle(context);

		if (this.getModule() instanceof RepairMaterialListModule module)
		{
			var player = context.getSender();

			if (this.add)
			{
				if (module.add(this.item))
				{
					SoundUtils.playSuccessSound(player, player.blockPosition());
					MessageUtils.format(TranslationConstants.MESSAGE_RECIPE_SAVED).sendTo(player);
				}
				else
				{
					SoundUtils.playErrorSound(player, player.blockPosition());
					MessageUtils.format(TranslationConstants.UNABLE_TO_ADD_RECIPE_MESSAGE, Component.translatable(module.getBuilding().getBuildingDisplayName())).sendTo(player);
				}

			}
			else if (module.remove(this.materialId))
			{
				SoundUtils.playSuccessSound(player, player.blockPosition());
			}

		}

	}

	public MaterialId getMaterialId()
	{
		return this.materialId;
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
