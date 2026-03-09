package steve_gall.minecolonies_compatibility.module.client.cgm.jei;

import com.mrcrayfish.guns.jei.GunModPlugin;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import steve_gall.minecolonies_compatibility.module.client.cgm.WorkbenchTeachScreen;
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

		registration.addRecipeClickArea(WorkbenchTeachScreen.class, 91, 36, 22, 15, GunModPlugin.WORKBENCH);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
	{
		if (!this.isLoaded())
		{
			return;
		}

		var recipeType = GunModPlugin.WORKBENCH;
		var transferHelper = registration.getTransferHelper();
		registration.addRecipeTransferHandler(new WorkbenchRecipeTransferHandler(transferHelper, recipeType), recipeType);
	}

	@Override
	public OptionalModule<?> getModule()
	{
		return ModuleManager.CGM;
	}

}
