package steve_gall.minecolonies_compatibility.module.client.storagenetwork;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.core.client.gui.NetworkStorageViewScreenUtils;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.storagenetwork.CitizenInventoryMenu;

public class CitizenInventoryScreen extends AbstractContainerScreen<CitizenInventoryMenu>
{
	private static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/citizen_inventory.png");

	public CitizenInventoryScreen(CitizenInventoryMenu containerMenu, Inventory inventory, Component title)
	{
		super(containerMenu, inventory, title);

		this.imageWidth = 176;
		this.imageHeight = 137;
		this.titleLabelX = 7;
		this.titleLabelY = 7;
		this.inventoryLabelX = 7;
		this.inventoryLabelY = 43;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(guiGraphics);

		super.render(guiGraphics, mouseX, mouseY, partialTicks);

		var view = this.getMenu().getBlockEntity().getView();
		guiGraphics.drawString(this.font, NetworkStorageViewScreenUtils.getModuleText(view), this.leftPos + 14, this.topPos + 21, 0xFF404040, false);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
		guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

}
