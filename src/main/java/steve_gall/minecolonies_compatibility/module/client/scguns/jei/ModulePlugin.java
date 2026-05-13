package steve_gall.minecolonies_compatibility.module.client.scguns.jei;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import steve_gall.minecolonies_compatibility.module.client.jei.AbstractModulePlugin;
import steve_gall.minecolonies_compatibility.module.client.scguns.GunBenchTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.OptionalModule;
import top.ribs.scguns.compat.GunBenchCategory;

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

		var recipeType = GunBenchCategory.GUN_BENCH_TYPE;
		registration.addRecipeClickArea(GunBenchTeachScreen.class, 106, 44, 22, 15, recipeType);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
	{
		if (!this.isLoaded())
		{
			return;
		}

		var transferHelper = registration.getTransferHelper();
		var recipeType = GunBenchCategory.GUN_BENCH_TYPE;
		registration.addRecipeTransferHandler(new GunBenchRecipeTransferHandler(transferHelper, recipeType), recipeType);
	}

	@Override
	public OptionalModule<?> getModule()
	{
		return ModuleManager.SCORCHED_GUNS;
	}

}
