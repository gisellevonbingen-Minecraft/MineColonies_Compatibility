package steve_gall.minecolonies_compatibility.module.client.refinedstorage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.RedstoneModeSideButton;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.core.client.gui.NetworkStorageViewScreenUtils;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.CitizenGridBlockEntity;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.CitizenGridContainerMenu;

public class CitizenGridScreen extends BaseScreen<CitizenGridContainerMenu>
{
	private static final Component TEXT_INVENTORY = Component.translatable("container.inventory");

	public CitizenGridScreen(CitizenGridContainerMenu containerMenu, Inventory inventory, Component title)
	{
		super(containerMenu, 176, 137, inventory, title);
	}

	@Override
	public void onPostInit(int x, int y)
	{
		this.addSideButton(new RedstoneModeSideButton(this, NetworkNodeBlockEntity.REDSTONE_MODE));
	}

	@Override
	public void renderBackground(PoseStack poseStack, int x, int y, int mouseX, int mouseY)
	{
		this.bindTexture(MineColoniesCompatibility.MOD_ID, "gui/citizen_grid.png");
		this.blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
	}

	@Override
	public void renderForeground(PoseStack poseStack, int mouseX, int mouseY)
	{
		this.renderString(poseStack, 7, 7, this.title.getString());
		this.renderString(poseStack, 7, 43, TEXT_INVENTORY.getString());

		var view = ((CitizenGridBlockEntity) this.getMenu().getBlockEntity()).getNode().getView();
		this.renderString(poseStack, 14, 21, NetworkStorageViewScreenUtils.getModuleText(view).getString());
	}

	@Override
	public void tick(int x, int y)
	{

	}

}
