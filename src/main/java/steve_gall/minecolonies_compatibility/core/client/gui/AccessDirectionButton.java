package steve_gall.minecolonies_compatibility.core.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.building.module.AccessDirection;

public class AccessDirectionButton extends Button
{
	public static final ResourceLocation WIDGETS_LOCATION = MineColoniesCompatibility.rl("textures/gui/access_direction.png");

	private AccessDirection accessDirection;

	public AccessDirectionButton(int x, int y, OnPress onPress)
	{
		super(x, y, 20, 20, Component.empty(), onPress);
	}

	@Override
	public void renderButton(PoseStack postStack, int mouseX, int mouseY, float partialTicks)
	{
		super.renderButton(postStack, mouseX, mouseY, partialTicks);

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		this.blit(postStack, this.x, this.y, this.getU(), 0, this.width, this.height);
	}

	protected int getU()
	{
		return switch (this.accessDirection)
		{
			case INSERT -> 0;
			case EXTRACT -> 20;
			default -> 40;
		};
	}

	public Component getTooltipText()
	{
		return Component.translatable("minecolonies_compatibility.text.access_direction." + this.accessDirection.name().toLowerCase());
	}

	public AccessDirection getAccessDirection()
	{
		return this.accessDirection;
	}

	public void setAccessDirection(AccessDirection accessDirection)
	{
		this.accessDirection = accessDirection;
	}

}
