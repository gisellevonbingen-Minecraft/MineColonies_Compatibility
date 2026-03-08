package steve_gall.minecolonies_compatibility.module.client.tacz.jei;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.crafting.GunSmithTableRecipe;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import steve_gall.minecolonies_compatibility.module.client.jei.AbstractModulePlugin;
import steve_gall.minecolonies_compatibility.module.client.tacz.GunSmithTableTeachScreen;
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

		var types = TimelessAPI.getAllCommonBlockIndex().stream().map(entry -> getRecipeType(entry.getKey())).toArray(RecipeType[]::new);
		registration.addRecipeClickArea(GunSmithTableTeachScreen.class, 91, 36, 22, 15, types);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
	{
		if (!this.isLoaded())
		{
			return;
		}

		var transferHelper = registration.getTransferHelper();

		for (var entry : TimelessAPI.getAllCommonBlockIndex())
		{

			var recipeType = getRecipeType(entry.getKey());
			var recipeTransferHandler = new GunSmithTableRecipeTransferHandler(transferHelper, recipeType);
			registration.addRecipeTransferHandler(recipeTransferHandler, recipeType);
		}

	}

	private RecipeType<RecipeHolder<GunSmithTableRecipe>> getRecipeType(ResourceLocation blockId)
	{
		return RecipeType.createRecipeHolderType(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "gun_smith_table/" + blockId.toString().replace(':', '_')));
	}

	@Override
	public OptionalModule<?> getModule()
	{
		return ModuleManager.TACZ;
	}

}
