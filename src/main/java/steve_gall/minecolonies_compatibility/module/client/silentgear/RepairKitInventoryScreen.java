package steve_gall.minecolonies_compatibility.module.client.silentgear;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.silentgear.menu.RepairKitInventoryMenu;
import steve_gall.minecolonies_tweaks.core.client.gui.CloseableContainerScreenExtension;

public class RepairKitInventoryScreen extends AbstractContainerScreen<RepairKitInventoryMenu> implements CloseableContainerScreenExtension
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/silentgear_repair_kit_inventory.png");

	private Screen parent;

	public RepairKitInventoryScreen(RepairKitInventoryMenu menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);
	}

	@Override
	public void minecolonies_tweaks$onInit(int leftPos, int topPos, int imageWidth, int imageHeight, addCloseButton addCloseButton)
	{
		addCloseButton.invoke(leftPos + imageWidth - 20, topPos - 5, 20, 20);
	}

	@Override
	public void minecolonies_tweaks$setParent(Screen screen)
	{
		this.parent = screen;
	}

	@Override
	public Screen minecolonies_tweaks$getParent()
	{
		return this.parent;
	}

	@Override
	public void render(PoseStack graphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(graphics);

		super.render(graphics, mouseX, mouseY, partialTicks);

		this.renderTooltip(graphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack graphics, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);

		blit(graphics, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

}
