package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init;

import com.minecolonies.api.crafting.registry.CraftingType;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.satisfy.vinery.core.registry.RecipeTypesRegistry;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleCraftingType;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting.ApplePressFermentingGenericRecipe;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting.ApplePressMashingGenericRecipe;
import steve_gall.minecolonies_tweaks.api.registries.DeferredRegisterHelper;

public class ModuleCraftingTypes
{
	public static final DeferredRegister<CraftingType> REGISTER = DeferredRegisterHelper.craftingTypes(MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<CraftingType, CraftingType> APPLE_PRESS_MASHING = DeferredRegisterHelper.registerCraftingType(REGISTER, "lets_do_vinery_apple_press_mashing", id -> new SimpleCraftingType<>(id, RecipeTypesRegistry.APPLE_PRESS_MASHING_RECIPE_TYPE, ApplePressMashingGenericRecipe::new));
	public static final DeferredHolder<CraftingType, CraftingType> APPLE_PRESS_FERMENTING = DeferredRegisterHelper.registerCraftingType(REGISTER, "lets_do_vinery_apple_press_fermenting", id -> new SimpleCraftingType<>(id, RecipeTypesRegistry.APPLE_PRESS_FERMENTING_RECIPE_TYPE, ApplePressFermentingGenericRecipe::new));

	private ModuleCraftingTypes()
	{

	}

}
