package steve_gall.minecolonies_compatibility.module.client.lets_do_vinery.jei;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.satisfy.vinery.core.compat.jei.category.ApplePressFermentingCategory;
import net.satisfy.vinery.core.compat.jei.category.ApplePressMashingCategory;
import steve_gall.minecolonies_compatibility.module.client.jei.AbstractModulePlugin;
import steve_gall.minecolonies_compatibility.module.client.lets_do_vinery.ApplePressFermentingTeachScreen;
import steve_gall.minecolonies_compatibility.module.client.lets_do_vinery.ApplePressMashingTeachScreen;
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

		registration.addRecipeClickArea(ApplePressMashingTeachScreen.class, 77, 34, 22, 15, ApplePressMashingCategory.APPLE_PRESS_MASHING_TYPE);
		registration.addRecipeClickArea(ApplePressFermentingTeachScreen.class, 77, 34, 22, 15, ApplePressFermentingCategory.APPLE_PRESS_TYPE);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
	{
		if (!this.isLoaded())
		{
			return;
		}

		var transferHelper = registration.getTransferHelper();
		registration.addRecipeTransferHandler(new ApplePressMashingTeachRecipeTransferHandler(transferHelper), ApplePressMashingCategory.APPLE_PRESS_MASHING_TYPE);
		registration.addRecipeTransferHandler(new ApplePressFermentingTeachRecipeTransferHandler(transferHelper), ApplePressFermentingCategory.APPLE_PRESS_TYPE);
	}

	@Override
	public OptionalModule<?> getModule()
	{
		return ModuleManager.LETS_DO_VINERY;
	}

}
