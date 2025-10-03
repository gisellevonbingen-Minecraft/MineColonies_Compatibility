package steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.init;

import com.minecolonies.api.crafting.registry.CraftingType;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.satisfy.meadow.core.registry.RecipeRegistry;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleCraftingType;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.crafting.CheeseGenericRecipe;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.crafting.CookingGenericRecipe;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.crafting.WoodcuttingGenericRecipe;
import steve_gall.minecolonies_tweaks.api.registries.DeferredRegisterHelper;

public class ModuleCraftingTypes
{
	public static final DeferredRegister<CraftingType> REGISTER = DeferredRegisterHelper.craftingTypes(MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<CraftingType, CraftingType> CHEESE = DeferredRegisterHelper.registerCraftingType(REGISTER, "lets_do_meadow_cheese", id -> new SimpleCraftingType<>(id, RecipeRegistry.CHEESE, CheeseGenericRecipe::new));
	public static final DeferredHolder<CraftingType, CraftingType> COOKING = DeferredRegisterHelper.registerCraftingType(REGISTER, "lets_do_meadow_cooking", id -> new SimpleCraftingType<>(id, RecipeRegistry.COOKING, CookingGenericRecipe::new));
	public static final DeferredHolder<CraftingType, CraftingType> WOODCUTTING = DeferredRegisterHelper.registerCraftingType(REGISTER, "lets_do_meadow_woodcutting", id -> new SimpleCraftingType<>(id, RecipeRegistry.WOODCUTTING, WoodcuttingGenericRecipe::new));

	private ModuleCraftingTypes()
	{

	}

}
