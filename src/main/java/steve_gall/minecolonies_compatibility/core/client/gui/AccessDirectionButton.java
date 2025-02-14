package steve_gall.minecolonies_compatibility.core.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
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
		super(Button.builder(Component.empty(), onPress).bounds(x, y, 20, 20));
	}

	@Override
	protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderWidget(graphics, mouseX, mouseY, partialTicks);

		graphics.blit(WIDGETS_LOCATION, this.getX(), this.getY(), this.getU(), 0, this.width, this.height);
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
