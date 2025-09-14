package steve_gall.minecolonies_compatibility.core.client.gui;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;

import com.ldtteam.blockui.controls.Button;
import com.ldtteam.blockui.controls.Text;
import com.minecolonies.api.util.constant.WindowConstants;
import com.minecolonies.core.client.gui.AbstractModuleWindow;

import net.minecraft.network.chat.Component;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModuleView;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.RestrictGiveToolMessage;

public class RestrictableModuleWindow extends AbstractModuleWindow
{
	private final IRestrictableModuleView module;

	public RestrictableModuleWindow(String res, IRestrictableModuleView module)
	{
		super(module.getBuildingView(), res);

		this.module = module;

		this.window.findPaneOfTypeByID(WindowConstants.DESC_LABEL, Text.class).setText(Component.translatable(module.getDesc().toLowerCase(Locale.US)));
	}

	@Override
	public void onOpened()
	{
		super.onOpened();
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		var button = this.window.findPaneOfTypeByID(WindowConstants.BUTTON_TRIGGER, Button.class);
		button.setText(Component.translatable(this.module.isRestrictEnabled() ? WindowConstants.ON : WindowConstants.OFF));
	}

	@Override
	public void onButtonClicked(@NotNull Button button)
	{
		super.onButtonClicked(button);

		if (button.getID().equals(WindowConstants.BUTTON_TRIGGER))
		{
			this.module.setRestrictEnabled(!this.module.isRestrictEnabled());
		}
		else if (button.getID().equals("giveTool"))
		{
			MineColoniesCompatibility.network().sendToServer(new RestrictGiveToolMessage(this.module, this.module.getDesc()));
		}

	}

	public IRestrictableModuleView getModule()
	{
		return this.module;
	}

}
