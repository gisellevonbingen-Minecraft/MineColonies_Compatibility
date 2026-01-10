package steve_gall.minecolonies_compatibility.module.common.farmersdelight.init;

import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleCraftingType;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CookingGenericRecipe;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CuttingCraftingType;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.PlatingCraftingType;
import steve_gall.minecolonies_tweaks.api.registries.DeferredRegisterHelper;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class ModuleCraftingTypes
{
	public static final DeferredRegister<CraftingType> REGISTER = DeferredRegisterHelper.craftingTypes(MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<CraftingType> CUTTING = DeferredRegisterHelper.registerCraftingType(REGISTER, "farmers_cutting", CuttingCraftingType::new);
	public static final RegistryObject<CraftingType> COOKING = DeferredRegisterHelper.registerCraftingType(REGISTER, "farmers_cooking", id -> new SimpleCraftingType<>(id, ModRecipeTypes.COOKING, CookingGenericRecipe::new));
	public static final RegistryObject<CraftingType> PLATING = DeferredRegisterHelper.registerCraftingType(REGISTER, "farmers_plating", PlatingCraftingType::new);

	private ModuleCraftingTypes()
	{

	}

}
