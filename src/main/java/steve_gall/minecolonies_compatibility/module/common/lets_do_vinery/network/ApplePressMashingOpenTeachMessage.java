package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.network;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.ModuleMenuOpenMessage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.menu.ApplePressMashingTeachMenu;

public class ApplePressMashingOpenTeachMessage extends ModuleMenuOpenMessage
{
	public static final CustomPacketPayload.Type<ApplePressMashingOpenTeachMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("let_do_vinery_apple_press_mashing_open_teach"));

	public ApplePressMashingOpenTeachMessage(IBuildingModuleView module)
	{
		super(module);
	}

	public ApplePressMashingOpenTeachMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);
	}

	@Override
	public CustomPacketPayload.Type<ApplePressMashingOpenTeachMessage> type()
	{
		return TYPE;
	}

	@Override
	protected AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player, IBuildingModule module)
	{
		return new ApplePressMashingTeachMenu(windowId, inventory, module);
	}

	@Override
	protected void toBuffer(RegistryFriendlyByteBuf buffer, IBuildingModule module)
	{
		super.toBuffer(buffer, module);
	}

}
