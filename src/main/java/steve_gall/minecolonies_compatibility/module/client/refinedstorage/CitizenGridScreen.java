package steve_gall.minecolonies_compatibility.module.client.refinedstorage;

import com.refinedmods.refinedstorage.common.storage.AccessModeSideButtonWidget;
import com.refinedmods.refinedstorage.common.storage.StoragePropertyTypes;
import com.refinedmods.refinedstorage.common.support.AbstractBaseScreen;
import com.refinedmods.refinedstorage.common.support.containermenu.PropertyTypes;
import com.refinedmods.refinedstorage.common.support.widget.RedstoneModeSideButtonWidget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.core.client.gui.NetworkStorageViewScreenUtils;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.CitizenGridContainerMenu;

public class CitizenGridScreen extends AbstractBaseScreen<CitizenGridContainerMenu>
{
	private static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/citizen_grid.png");

	public CitizenGridScreen(CitizenGridContainerMenu containerMenu, Inventory inventory, Component title)
	{
		super(containerMenu, inventory, title);
		this.imageWidth = 176;
		this.imageHeight = 137;
		this.inventoryLabelY = 43;
	}

	@Override
	protected void init()
	{
		super.init();

		this.addSideButton(new RedstoneModeSideButtonWidget(getMenu().getProperty(PropertyTypes.REDSTONE_MODE)));
		this.addSideButton(new AccessModeSideButtonWidget(getMenu().getProperty(StoragePropertyTypes.ACCESS_MODE)));
	}

	@Override
	protected ResourceLocation getTexture()
	{
		return TEXTURE;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta)
	{
		super.render(graphics, mouseX, mouseY, delta);

		graphics.drawString(this.font, NetworkStorageViewScreenUtils.getModuleText(this.menu.getLinkedPos()).getString(), this.leftPos + 14, this.topPos + 21, 0xFF404040, false);
	}

}
