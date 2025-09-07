package steve_gall.minecolonies_compatibility.core.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.inventory.AccessDirectionHolderMenu;
import steve_gall.minecolonies_compatibility.core.common.network.message.AccessDirectionMessage;

public class AccessDirectionHolderScreen extends AbstractContainerScreen<AccessDirectionHolderMenu<?>>
{
	private static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/access_direction_holder.png");

	private AccessDirectionButton accessDirectionButton;

	public AccessDirectionHolderScreen(AccessDirectionHolderMenu<?> containerMenu, Inventory inventory, Component title)
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
	protected void init()
	{
		super.init();

		var blockEntity = this.getMenu().getBlockEntity();
		this.accessDirectionButton = this.addRenderableWidget(new AccessDirectionButton(this.leftPos - 22, this.topPos, b ->
		{
			var next = blockEntity.getAccessDirection().next();
			blockEntity.setAccessDirection(next);
			PacketDistributor.sendToServer(new AccessDirectionMessage<>(blockEntity, next));
		}));

		this.accessDirectionButton.setAccessDirection(blockEntity.getAccessDirection());
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);

		var blockEntity = this.getMenu().getBlockEntity();
		guiGraphics.drawString(this.font, NetworkStorageViewScreenUtils.getModuleText(blockEntity.getNetworkStorageView()), this.leftPos + 14, this.topPos + 21, 0xFF404040, false);

		if (this.accessDirectionButton != null)
		{
			var accessDirection = blockEntity.getAccessDirection();
			this.accessDirectionButton.setAccessDirection(accessDirection);

			if (this.accessDirectionButton.isMouseOver(mouseX, mouseY))
			{
				guiGraphics.renderTooltip(this.font, this.accessDirectionButton.getTooltipText(), mouseX, mouseY);
			}

		}

		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
		guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

}
