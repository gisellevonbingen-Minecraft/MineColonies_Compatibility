package steve_gall.minecolonies_compatibility.module.client.lets_do_vinery.jei;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import steve_gall.minecolonies_compatibility.module.client.jei.AbstractModulePlugin;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.OptionalModule;

@JeiPlugin
public class ModulePlugin extends AbstractModulePlugin
{
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration)
	{
		if (!this.isLoaded())
		{
			return;
		}

		// registration.addRecipeClickArea(ApplePressMashingTeachScreen.class, 77, 34, 22, 15, ApplePressCategory.APPLE_PRESS);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
	{
		if (!this.isLoaded())
		{
			return;
		}

		var transferHelper = registration.getTransferHelper();
		// registration.addRecipeTransferHandler(new ApplePressMashingTeachRecipeTransferHandler(transferHelper), ApplePressCategory.APPLE_PRESS);
	}

	@Override
	public OptionalModule<?> getModule()
	{
		return ModuleManager.LETS_DO_VINERY;
	}

}
