package steve_gall.minecolonies_compatibility.api.client.jei;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.core.common.network.message.JEIGhostAcceptItemMessage;

public abstract class ItemGhostTarget extends GhostTarget<ItemStack>
{
	public ItemGhostTarget(int x, int y, int slotNumber)
	{
		super(x, y, slotNumber);
	}

	public abstract boolean isVirtual();

	@Override
	public void accept(ItemStack ingredient)
	{
		PacketDistributor.sendToServer(new JEIGhostAcceptItemMessage(this.getSlotNumber(), ingredient, this.isVirtual()));
	}

}
