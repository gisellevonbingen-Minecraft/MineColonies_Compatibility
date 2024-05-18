package steve_gall.minecolonies_compatibility.module.common.farmersdelight.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.TeachCookingMenu;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.TeachCuttingMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<MenuType<TeachCuttingMenu>> TEACH_CUTTING = REGISTER.register("teach_cutting", () -> IForgeMenuType.create(TeachCuttingMenu::new));
	public static final RegistryObject<MenuType<TeachCookingMenu>> TEACH_COOKING = REGISTER.register("teach_cooking", () -> IForgeMenuType.create(TeachCookingMenu::new));

	private ModuleMenuTypes()
	{

	}

}
