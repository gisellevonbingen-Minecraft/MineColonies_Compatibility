package steve_gall.minecolonies_compatibility.core.client.gui;

import org.jetbrains.annotations.NotNull;

import com.ldtteam.blockui.controls.Button;
import com.ldtteam.blockui.controls.ButtonImage;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.core.client.gui.modules.WindowListRecipes;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.building.module.SmithingTemplateCraftingModuleView;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackCounter;
import steve_gall.minecolonies_compatibility.core.common.network.message.SmithingTemplateOpenInventoryMessage;

public class WindowListSmithingTemplateRecipes extends WindowListRecipes
{
	private final SmithingTemplateCraftingModuleView module;
	private final ButtonImage inventoryButton;

	public WindowListSmithingTemplateRecipes(IBuildingView view, String name, SmithingTemplateCraftingModuleView module)
	{
		super(view, name, module);
		this.module = module;

		var button = this.inventoryButton = new ButtonImage();
		button.setID(MineColoniesCompatibility.rl("smithing_template_inventory").toString());
		button.setColors(0xFF000000, 0xFF000000, 0xFF000000);
		button.setImage(ResourceLocation.parse("minecolonies:textures/gui/builderhut/builder_button_medium_large.png"));
		button.setText(Component.translatable("minecolonies_compatibility.text.smithing_template_inventory"));
		button.setSize(129, 17);
		button.setTextSize(129, 17);
		button.setPosition(30, 217);
		this.addChild(button);
	}

	@Override
	public void onButtonClicked(@NotNull Button button)
	{
		super.onButtonClicked(button);

		if (button == this.inventoryButton)
		{
			PacketDistributor.sendToServer(new SmithingTemplateOpenInventoryMessage(this.module));
		}

	}

	public ItemStackCounter getCounter()
	{
		return this.module.getCounter();
	}

}
