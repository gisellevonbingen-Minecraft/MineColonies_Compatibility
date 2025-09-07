package steve_gall.minecolonies_compatibility.core.common.network.message;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.building.module.SmithingTemplateCraftingModule;
import steve_gall.minecolonies_compatibility.core.common.inventory.SmithingTemplateInventoryMenu;

public class SmithingTemplateOpenInventoryMessage extends ModuleMenuOpenMessage
{
	public static final CustomPacketPayload.Type<SmithingTemplateOpenInventoryMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("smithing_template_open_inventory"));

	public SmithingTemplateOpenInventoryMessage(IBuildingModuleView module)
	{
		super(module);
	}

	public SmithingTemplateOpenInventoryMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);
	}

	@Override
	protected AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player, IBuildingModule module)
	{
		return new SmithingTemplateInventoryMenu(windowId, inventory, (SmithingTemplateCraftingModule) module);
	}

	@Override
	protected Component getDisplayName()
	{
		return Component.translatable("minecolonies_compatibility.text.smithing_template_inventory");
	}

	@Override
	protected void toBuffer(RegistryFriendlyByteBuf buffer, IBuildingModule module)
	{
		super.toBuffer(buffer, module);
	}

	@Override
	public CustomPacketPayload.Type<SmithingTemplateOpenInventoryMessage> type()
	{
		return TYPE;
	}

}
