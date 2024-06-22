package steve_gall.minecolonies_compatibility.module.client.ae2;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.ScreenStyle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.module.common.ae2.CitizenTerminalMenu;
import steve_gall.minecolonies_compatibility.module.common.ae2.CitizenTerminalPart;

public class CitizenTerminalScreen extends AEBaseScreen<CitizenTerminalMenu>
{
	private static final String TEXT_ID_LINK = "link";
	private static final Component TEXT_NOT_LINKED = Component.translatable("minecolonies_compatibility.gui.not_linked");

	public CitizenTerminalScreen(CitizenTerminalMenu menu, Inventory playerInventory, Component title, ScreenStyle style)
	{
		super(menu, playerInventory, title, style);

		var part = (CitizenTerminalPart) this.getMenu().getTarget();
		this.setTextContent(TEXT_ID_DIALOG_TITLE, part.getPartItem().asItem().getDescription());
	}

	@Override
	protected void updateBeforeRender()
	{
		super.updateBeforeRender();

		var view = ((CitizenTerminalPart) this.getMenu().getTarget()).getView();
		var module = view.getLinkedModuleView();

		if (module != null)
		{
			var pos = module.getBuildingView().getID();
			this.setTextContent(TEXT_ID_LINK, Component.translatable("minecolonies_compatibility.gui.linked_pos", pos.getX(), pos.getY(), pos.getZ()));
		}
		else
		{
			this.setTextContent(TEXT_ID_LINK, TEXT_NOT_LINKED);
		}

	}

}
