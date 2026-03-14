package steve_gall.minecolonies_compatibility.module.common.scguns.init;

import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleCraftingType;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.scguns.crafting.GunBenchGenericRecipe;
import steve_gall.minecolonies_tweaks.api.registries.DeferredRegisterHelper;
import top.ribs.scguns.client.screen.GunBenchRecipe;

public class ModuleCraftingTypes
{
	public static final DeferredRegister<CraftingType> REGISTER = DeferredRegisterHelper.craftingTypes(MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<CraftingType> GUN_BENCH = DeferredRegisterHelper.registerCraftingType(REGISTER, "scguns_gun_bench", id -> new SimpleCraftingType<>(id, () -> GunBenchRecipe.Type.INSTANCE, GunBenchGenericRecipe::new));

	private ModuleCraftingTypes()
	{

	}

}
