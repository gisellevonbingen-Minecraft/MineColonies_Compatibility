package steve_gall.minecolonies_compatibility.module.common.cgm.init;

import com.minecolonies.api.crafting.registry.CraftingType;
import com.mrcrayfish.guns.init.ModRecipeTypes;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleCraftingType;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.cgm.crafting.WorkbenchGenericRecipe;
import steve_gall.minecolonies_tweaks.api.registries.DeferredRegisterHelper;

public class ModuleCraftingTypes
{
	public static final DeferredRegister<CraftingType> REGISTER = DeferredRegisterHelper.craftingTypes(MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<CraftingType> WORKBENCH = DeferredRegisterHelper.registerCraftingType(REGISTER, "cgm_workbench", id -> new SimpleCraftingType<>(id, ModRecipeTypes.WORKBENCH, WorkbenchGenericRecipe::new));

	private ModuleCraftingTypes()
	{

	}

}
