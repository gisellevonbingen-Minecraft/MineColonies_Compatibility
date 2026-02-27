package steve_gall.minecolonies_compatibility.module.client.create;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.core.client.gui.NetworkStorageViewScreenUtils;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.create.AddressMessage;
import steve_gall.minecolonies_compatibility.module.common.create.CitizenStockKeeperMenu;

public class CitizenStockKeeperScreen extends AbstractContainerScreen<CitizenStockKeeperMenu>
{
	private static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/citizen_stock_keeper.png");

	private EditBox addressBox;
	private String address;

	public CitizenStockKeeperScreen(CitizenStockKeeperMenu menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);

		this.imageWidth = 176;
		this.imageHeight = 147;
		this.titleLabelX = 7;
		this.titleLabelY = 7;
		this.inventoryLabelX = 7;
		this.inventoryLabelY = 53;

		this.address = menu.getBlockEntity().getAddress();
	}

	@Override
	protected void init()
	{
		super.init();

		var blockEntity = this.getMenu().getBlockEntity();
		this.addressBox = new EditBox(this.font, this.leftPos + this.titleLabelX, this.topPos + 35, 120, 15, Component.literal(blockEntity.toString()));
		this.addressBox.setValue(this.address);
		this.addressBox.setHint(CreateLang.translate("gui.stock_keeper.package_adress").style(ChatFormatting.ITALIC).style(ChatFormatting.DARK_GRAY).component());
		this.addRenderableWidget(this.addressBox);
	}

	@Override
	protected void containerTick()
	{
		super.containerTick();

		var newAddress = this.addressBox.getValue();

		if (!this.address.equals(newAddress))
		{
			this.address = newAddress;
			PacketDistributor.sendToServer(new AddressMessage(this.getMenu().getBlockEntity(), newAddress));
		}

	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);

		var blockEntity = this.getMenu().getBlockEntity();
		guiGraphics.drawString(this.font, NetworkStorageViewScreenUtils.getModuleText(blockEntity.getNetworkStorageView().getLinkedPos()), this.leftPos + 14, this.topPos + 21, 0xFF404040, false);

		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
		guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

	@Override
	protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		super.renderTooltip(guiGraphics, mouseX, mouseY);

		if (!this.addressBox.isFocused() && this.addressBox.isHovered())
		{
			guiGraphics.renderComponentTooltip(this.font, List.of(CreateLang.translate("gui.factory_panel.restocker_address").color(ScrollInput.HEADER_RGB).component(), CreateLang.translate("gui.schedule.lmb_edit").style(ChatFormatting.DARK_GRAY).style(ChatFormatting.ITALIC).component()), mouseX, mouseY);
		}

	}

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers)
	{
		if (this.getFocused() == this.addressBox && this.addressBox.isFocused())
		{
			if (this.minecraft.options.keyInventory.isActiveAndMatches(InputConstants.getKey(pKeyCode, pScanCode)))
			{
				return true;
			}
			else if (pKeyCode == GLFW.GLFW_KEY_ENTER || pKeyCode == GLFW.GLFW_KEY_KP_ENTER)
			{
				this.addressBox.setFocused(false);
				return true;
			}

		}

		return super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}

	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
	{
		boolean rmb = pButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT;

		if (rmb && this.addressBox.isMouseOver(pMouseX, pMouseY))
		{
			this.addressBox.setValue("");
			return true;
		}

		if (this.addressBox.isFocused())
		{
			boolean result = this.addressBox.mouseClicked(pMouseX, pMouseY, pButton);
			if (this.addressBox.isHovered() || result)
			{
				return result;
			}

			this.addressBox.setFocused(false);
		}

		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}

}
