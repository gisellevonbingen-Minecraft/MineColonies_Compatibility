package steve_gall.minecolonies_compatibility.module.client.storagenetwork;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);

		super.render(poseStack, mouseX, mouseY, partialTicks);

		var view = this.getMenu().getBlockEntity().getView();
		this.font.draw(poseStack, NetworkStorageViewScreenUtils.getModuleText(view), this.leftPos + 14, this.topPos + 21, 0xFF404040);
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
