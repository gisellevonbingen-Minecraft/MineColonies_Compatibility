package steve_gall.minecolonies_compatibility.mixin.client.blockui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ldtteam.blockui.PaneParams;
import com.ldtteam.blockui.views.View;

import steve_gall.minecolonies_compatibility.core.client.gui.ViewOverrideExtension;

@Mixin(value = View.class, remap = false)
public abstract class ViewMixin
{
	@Inject(method = "parseChildren", remap = false, at = @At(value = "TAIL"), cancellable = true)
	private void parseChildren(PaneParams params, CallbackInfo ci)
	{
		if (this instanceof ViewOverrideExtension extensions)
		{
			extensions.minecolonies_compatibility$onParse((View) (Object) this, params);
		}

	}

}
