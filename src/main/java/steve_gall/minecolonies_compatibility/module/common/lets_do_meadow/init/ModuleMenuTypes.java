package steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.menu.CheeseTeachMenu;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.menu.CookingTeachMenu;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.menu.WoodcuttingTeachMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<MenuType<CheeseTeachMenu>> CHEESE_TEACH = REGISTER.register("lets_do_meadow_cheese_teach", () -> IForgeMenuType.create(CheeseTeachMenu::new));
	public static final RegistryObject<MenuType<CookingTeachMenu>> COOKING_TEACH = REGISTER.register("lets_do_meadow_cooking_teach", () -> IForgeMenuType.create(CookingTeachMenu::new));
	public static final RegistryObject<MenuType<WoodcuttingTeachMenu>> WOODCUTTING_TEACH = REGISTER.register("lets_do_meadow_woodcutting_teach", () -> IForgeMenuType.create(WoodcuttingTeachMenu::new));

	private ModuleMenuTypes()
	{

	}

}
