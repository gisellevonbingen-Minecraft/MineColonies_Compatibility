package steve_gall.minecolonies_compatibility.module.client.tacz.jei;

import java.util.HashMap;

import com.tacz.guns.GunMod;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.crafting.GunSmithTableRecipe;
import com.tacz.guns.init.ModRecipe;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
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
		registration.addRecipeClickArea(GunSmithTableTeachScreen.class, 68, 35, 22, 15, types);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
	{
		if (!this.isLoaded())
		{
			return;
		}

		var recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModRecipe.GUN_SMITH_TABLE_CRAFTING.get());
		var recipeToHolderMap = new HashMap<GunSmithTableRecipe, RecipeHolder<GunSmithTableRecipe>>();

		for (var recipe : recipes)
		{
			recipeToHolderMap.put(recipe.value(), recipe);
		}

		var transferHelper = registration.getTransferHelper();

		for (var entry : TimelessAPI.getAllCommonBlockIndex())
		{

			var recipeType = getRecipeType(entry.getKey());
			var recipeTransferHandler = new GunSmithTableRecipeTransferHandler(transferHelper, recipeType, recipeToHolderMap);
			registration.addRecipeTransferHandler(recipeTransferHandler, recipeType);
		}

	}

	private RecipeType<GunSmithTableRecipe> getRecipeType(ResourceLocation blockId)
	{
		return RecipeType.create(GunMod.MOD_ID, "gun_smith_table/" + blockId.toString().replace(':', '_'), GunSmithTableRecipe.class);
	}

	@Override
	public OptionalModule<?> getModule()
	{
		return ModuleManager.TACZ;
	}

}
