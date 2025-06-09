package steve_gall.minecolonies_compatibility.module.common.tacz.init;

import com.minecolonies.api.crafting.registry.CraftingType;
import com.tacz.guns.init.ModRecipe;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleCraftingType;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.tacz.crafting.GunSmithTableGenericRecipe;
import steve_gall.minecolonies_tweaks.api.registries.DeferredRegisterHelper;

public class ModuleCraftingTypes
{
	public static final DeferredRegister<CraftingType> REGISTER = DeferredRegisterHelper.craftingTypes(MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<CraftingType> GUN_SMITH_TABLE = DeferredRegisterHelper.registerCraftingType(REGISTER, "tacz_gun_smith_table", id -> new SimpleCraftingType<>(id, ModRecipe.GUN_SMITH_TABLE_CRAFTING, GunSmithTableGenericRecipe::new));

	private ModuleCraftingTypes()
	{

	}

}
