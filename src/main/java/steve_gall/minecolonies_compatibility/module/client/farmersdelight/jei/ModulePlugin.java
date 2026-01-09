package steve_gall.minecolonies_compatibility.module.client.farmersdelight.jei;

import com.minecolonies.api.colony.jobs.ModJobs;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import steve_gall.minecolonies_compatibility.module.client.farmersdelight.CookingTeachScreen;
import steve_gall.minecolonies_compatibility.module.client.farmersdelight.CuttingTeachScreen;
import steve_gall.minecolonies_compatibility.module.client.farmersdelight.PlatingTeachScreen;
import steve_gall.minecolonies_compatibility.module.client.jei.AbstractModulePlugin;
import steve_gall.minecolonies_compatibility.module.client.jei.ModPlugin;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.OptionalModule;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;

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

		var recipeType = ModPlugin.createRecipeType(ModJobs.cookassistant.get());
		registration.addRecipeClickArea(CuttingTeachScreen.class, 40, 36, 22, 15, FDRecipeTypes.CUTTING);
		registration.addRecipeClickArea(CookingTeachScreen.class, 91, 26, 22, 15, FDRecipeTypes.COOKING);
		registration.addRecipeClickArea(PlatingTeachScreen.class, 77, 26, 22, 15, recipeType);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
	{
		if (!this.isLoaded())
		{
			return;
		}

		var transferHelper = registration.getTransferHelper();
		var recipeType = ModPlugin.createRecipeType(ModJobs.cookassistant.get());
		registration.addRecipeTransferHandler(new CuttingTeachRecipeTransferHandler(transferHelper), FDRecipeTypes.CUTTING);
		registration.addRecipeTransferHandler(new CookingTeachRecipeTransferHandler(transferHelper), FDRecipeTypes.COOKING);
		registration.addRecipeTransferHandler(new PlatingTeachRecipeTransferHandler(transferHelper, recipeType), recipeType);
	}

	@Override
	public OptionalModule<?> getModule()
	{
		return ModuleManager.FARMERSDELIGHT;
	}

}
