package steve_gall.minecolonies_compatibility.mixin.client.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.PaneBuilders;
import com.ldtteam.blockui.controls.Text;
import com.minecolonies.api.colony.buildings.modules.settings.ISetting;
import com.minecolonies.api.colony.buildings.modules.settings.ISettingKey;
import com.minecolonies.core.client.gui.modules.building.SettingsModuleWindow;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.ModList;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.util.ReflectionUtils;

@Mixin(targets = "com.minecolonies.core.client.gui.modules.SettingsModuleWindow$1", remap = false)
public class SettingsModuleWindow1Mixin
{
	@Unique
	private final SettingsModuleWindow minecolonies_compatibility$this$0 = ReflectionUtils.getOuter(this);

	@Inject(method = "updateElement", remap = false, at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private <S> void setting_render(int index, Pane rowPane, CallbackInfo ci, ISettingKey<? extends ISetting<S>> key, ISetting<S> setting)
	{
		var id = key.getUniqueId();

		if (id.getNamespace().equals(MineColoniesCompatibility.MOD_ID))
		{
			var rowDescriptionField = rowPane.findPaneOfTypeByID("desc", Text.class);

			if (rowDescriptionField != null && rowDescriptionField.getHoverPane() == null)
			{
				var ttBuilder = PaneBuilders.tooltipBuilder().hoverPane(rowDescriptionField);
				var settingTooltipKey = "com.minecolonies.coremod.setting." + id.toString() + ".tooltip";

				if (I18n.exists(settingTooltipKey))
				{
					ttBuilder.appendNL(Component.translatable(settingTooltipKey));
				}

				ttBuilder.appendNL(Component.literal(ModList.get().getModContainerById(id.getNamespace()).get().getModInfo().getDisplayName()).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
				rowDescriptionField.setHoverPane(ttBuilder.build());
			}

		}

	}

}
