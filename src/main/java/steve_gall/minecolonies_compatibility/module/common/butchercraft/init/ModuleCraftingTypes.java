package steve_gall.minecolonies_compatibility.module.common.butchercraft.init;

import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.crafting.GrinderCraftingType;
import steve_gall.minecolonies_tweaks.api.registries.DeferredRegisterHelper;

public class ModuleCraftingTypes
{
	public static final DeferredRegister<CraftingType> REGISTER = DeferredRegisterHelper.craftingTypes(MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<CraftingType> GRINDER = DeferredRegisterHelper.registerCraftingType(REGISTER, "butchercraft_grinder", GrinderCraftingType::new);

	private ModuleCraftingTypes()
	{

	}

}
