package steve_gall.minecolonies_compatibility.module.client.lets_do_meadow.jei;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
//import net.satisfy.meadow.core.compat.jei.category.WoodCutterCategory;
import net.satisfy.meadow.core.compat.jei.category.CheesePressCategory;
import net.satisfy.meadow.core.compat.jei.category.CookingCauldronCategory;
import steve_gall.minecolonies_compatibility.module.client.jei.AbstractModulePlugin;
import steve_gall.minecolonies_compatibility.module.client.lets_do_meadow.CheeseTeachScreen;
import steve_gall.minecolonies_compatibility.module.client.lets_do_meadow.CookingTeachScreen;
//import steve_gall.minecolonies_compatibility.module.client.lets_do_meadow.WoodcuttingTeachScreen;
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

		registration.addRecipeClickArea(CheeseTeachScreen.class, 91, 26, 22, 15, CheesePressCategory.CHEESE_PRESS);
		registration.addRecipeClickArea(CookingTeachScreen.class, 86, 26, 22, 15, CookingCauldronCategory.COOKING_CAULDRON);
		// registration.addRecipeClickArea(WoodcuttingTeachScreen.class, 77, 26, 22, 15, WoodCutterCategory.WOODCUTTER);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
	{
		if (!this.isLoaded())
		{
			return;
		}

		var transferHelper = registration.getTransferHelper();
		registration.addRecipeTransferHandler(new CheeseTeachRecipeTransferHandler(transferHelper), CheesePressCategory.CHEESE_PRESS);
		registration.addRecipeTransferHandler(new CookingTeachRecipeTransferHandler(transferHelper), CookingCauldronCategory.COOKING_CAULDRON);
		// registration.addRecipeTransferHandler(new WoodcuttingTeachRecipeTransferHandler(transferHelper), WoodCutterCategory.WOODCUTTER);
	}

	@Override
	public OptionalModule<?> getModule()
	{
		return ModuleManager.LETS_DO_MEADOW;
	}

}
