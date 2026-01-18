package steve_gall.minecolonies_compatibility.module.client.ae2;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Settings;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ServerSettingToggleButton;
import appeng.client.gui.widgets.SettingToggleButton;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.core.client.gui.NetworkStorageViewScreenUtils;
import steve_gall.minecolonies_compatibility.module.common.ae2.CitizenTerminalMenu;
import steve_gall.minecolonies_compatibility.module.common.ae2.CitizenTerminalPart;

public class CitizenTerminalScreen extends AEBaseScreen<CitizenTerminalMenu>
{
	private static final String TEXT_ID_LINK = "link";

	private final SettingToggleButton<AccessRestriction> accessButton;

	public CitizenTerminalScreen(CitizenTerminalMenu menu, Inventory playerInventory, Component title, ScreenStyle style)
	{
		super(menu, playerInventory, title, style);

		var part = (CitizenTerminalPart) menu.getTarget();
		this.setTextContent(TEXT_ID_DIALOG_TITLE, part.getPartItem().asItem().getDescription());

		this.accessButton = new ServerSettingToggleButton<>(Settings.ACCESS, AccessRestriction.READ_WRITE);
		this.addToLeftToolbar(this.accessButton);
	}

	@Override
	protected void updateBeforeRender()
	{
		super.updateBeforeRender();

		var part = (CitizenTerminalPart) this.getMenu().getTarget();
		this.setTextContent(TEXT_ID_LINK, NetworkStorageViewScreenUtils.getModuleText(part.getView().getLinkedPos()));

		this.accessButton.set(this.getMenu().getConfigManager().getSetting(Settings.ACCESS));
	}

}
