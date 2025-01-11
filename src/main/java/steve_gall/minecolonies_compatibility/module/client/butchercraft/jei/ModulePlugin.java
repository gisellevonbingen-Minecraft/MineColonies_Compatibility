package steve_gall.minecolonies_compatibility.module.client.butchercraft.jei;

import com.lance5057.butchercraft.integration.jei.categories.GrinderRecipeCategory;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import steve_gall.minecolonies_compatibility.module.client.butchercraft.GrinderTeachScreen;
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

		registration.addRecipeClickArea(GrinderTeachScreen.class, 86, 35, 22, 15, GrinderRecipeCategory.TYPE);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
	{
		if (!this.isLoaded())
		{
			return;
		}

		var transferHelper = registration.getTransferHelper();
		registration.addRecipeTransferHandler(new GrinderTeachRecipeTransferHandler(transferHelper), GrinderRecipeCategory.TYPE);
	}

	@Override
	public OptionalModule<?> getModule()
	{
		return ModuleManager.BUTCHERCRAFT;
	}

}
