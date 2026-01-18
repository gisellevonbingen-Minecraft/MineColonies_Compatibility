package steve_gall.minecolonies_compatibility.core.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
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
			MineColoniesCompatibility.network().sendToServer(new AccessDirectionMessage<>(blockEntity, next));
		}));

		this.accessDirectionButton.setAccessDirection(blockEntity.getAccessDirection());
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);

		super.render(poseStack, mouseX, mouseY, partialTicks);

		var blockEntity = this.getMenu().getBlockEntity();
		this.font.draw(poseStack, NetworkStorageViewScreenUtils.getModuleText(blockEntity.getNetworkStorageView().getLinkedPos()), this.leftPos + 14, this.topPos + 21, 0xFF404040);

		if (this.accessDirectionButton != null)
		{
			var accessDirection = blockEntity.getAccessDirection();
			this.accessDirectionButton.setAccessDirection(accessDirection);

			if (this.accessDirectionButton.isMouseOver(mouseX, mouseY))
			{
				this.renderTooltip(poseStack, this.accessDirectionButton.getTooltipText(), mouseX, mouseY);
			}

		}

		this.renderTooltip(poseStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);

		this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

}
