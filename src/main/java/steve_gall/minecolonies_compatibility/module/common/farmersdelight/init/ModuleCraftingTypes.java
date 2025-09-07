package steve_gall.minecolonies_compatibility.module.common.farmersdelight.init;

import com.minecolonies.api.crafting.registry.CraftingType;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleCraftingType;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CookingGenericRecipe;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CuttingCraftingType;
import steve_gall.minecolonies_tweaks.api.registries.DeferredRegisterHelper;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class ModuleCraftingTypes
{
	public static final DeferredRegister<CraftingType> REGISTER = DeferredRegisterHelper.craftingTypes(MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<CraftingType, CuttingCraftingType> CUTTING = DeferredRegisterHelper.registerCraftingType(REGISTER, "farmers_cutting", CuttingCraftingType::new);
	public static final DeferredHolder<CraftingType, CraftingType> COOKING = DeferredRegisterHelper.registerCraftingType(REGISTER, "farmers_cooking", id -> new SimpleCraftingType<>(id, ModRecipeTypes.COOKING, CookingGenericRecipe::new));

	private ModuleCraftingTypes()
	{

	}

}
