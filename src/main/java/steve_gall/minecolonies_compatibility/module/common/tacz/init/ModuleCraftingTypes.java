package steve_gall.minecolonies_compatibility.module.common.tacz.init;

import com.minecolonies.api.crafting.registry.CraftingType;
import com.tacz.guns.init.ModRecipe;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleCraftingType;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.tacz.crafting.GunSmithTableGenericRecipe;
import steve_gall.minecolonies_tweaks.api.registries.DeferredRegisterHelper;

public class ModuleCraftingTypes
{
	public static final DeferredRegister<CraftingType> REGISTER = DeferredRegisterHelper.craftingTypes(MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<CraftingType, CraftingType> GUN_SMITH_TABLE = DeferredRegisterHelper.registerCraftingType(REGISTER, "tacz_gun_smith_table", id -> new SimpleCraftingType<>(id, ModRecipe.GUN_SMITH_TABLE_CRAFTING, GunSmithTableGenericRecipe::new));

	private ModuleCraftingTypes()
	{

	}

}
