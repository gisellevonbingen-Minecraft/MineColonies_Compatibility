package steve_gall.minecolonies_compatibility.core.common.network.message;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.inventory.StonecutterTeachMenu;

public class StonecutterOpenTeachMessage extends ModuleMenuOpenMessage
{
	public static final CustomPacketPayload.Type<StonecutterOpenTeachMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("stonecutter_open_teach"));

	public StonecutterOpenTeachMessage(IBuildingModuleView module)
	{
		super(module);
	}

	public StonecutterOpenTeachMessage(RegistryFriendlyByteBuf buffer)
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
		return new StonecutterTeachMenu(windowId, inventory, module);
	}

	@Override
	public CustomPacketPayload.Type<StonecutterOpenTeachMessage> type()
	{
		return TYPE;
	}

}
