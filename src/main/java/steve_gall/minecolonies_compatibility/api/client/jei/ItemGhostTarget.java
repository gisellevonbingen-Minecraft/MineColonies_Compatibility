package steve_gall.minecolonies_compatibility.api.client.jei;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.JEIGhostAcceptMessage;

public class ItemGhostTarget extends GhostTarget<ItemStack>
{
	public ItemGhostTarget(@NotNull AbstractContainerScreen<?> screen, @NotNull Slot slot, int slotNumber)
	{
		super(screen, slot, slotNumber);
	}

	@Override
	public void accept(ItemStack ingredient)
	{
		MineColoniesCompatibility.network().sendToServer(new JEIGhostAcceptMessage(this.getSlotNumber(), ingredient));
	}

}
