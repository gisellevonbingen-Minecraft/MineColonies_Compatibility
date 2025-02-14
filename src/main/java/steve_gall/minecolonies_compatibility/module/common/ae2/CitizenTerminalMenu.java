package steve_gall.minecolonies_compatibility.module.common.ae2;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Setting;
import appeng.api.config.Settings;
import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigurableObject;
import appeng.core.sync.packets.ConfigValuePacket;
import appeng.menu.AEBaseMenu;
import appeng.util.ConfigManager;
import appeng.util.IConfigManagerListener;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.module.common.ae2.init.ModuleMenuTypes;

public class CitizenTerminalMenu extends AEBaseMenu implements IConfigurableObject, IConfigManagerListener
{
	private final ConfigManager config;

	public CitizenTerminalMenu(int windowId, Inventory inventory, CitizenTerminalPart part)
	{
		super(ModuleMenuTypes.CITIZEN_TERMINAL.get(), windowId, inventory, part);

		this.createPlayerInventorySlots(inventory);

		this.config = new ConfigManager(this);
		this.config.registerSetting(Settings.ACCESS, AccessRestriction.READ_WRITE);
	}

	@Override
	public void broadcastChanges()
	{
		super.broadcastChanges();

		var src = ((IConfigurableObject) this.getTarget()).getConfigManager();
		var dst = this.config;

		for (var setting : dst.getSettings())
		{
			var local = src.getSetting(setting);
			var remote = dst.getSetting(setting);

			if (local != remote)
			{
				setting.copy(src, dst);
				this.sendPacketToClient(new ConfigValuePacket(setting, src));
			}

		}

	}

	@Override
	public IConfigManager getConfigManager()
	{
		return this.config;
	}

	@Override
	public void onSettingChanged(IConfigManager manager, Setting<?> setting)
	{

	}

}
