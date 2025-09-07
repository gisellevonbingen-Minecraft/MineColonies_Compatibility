package steve_gall.minecolonies_compatibility.module.common.farmersdelight.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.CookingTeachMenu;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.CuttingTeachMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<MenuType<?>, MenuType<CuttingTeachMenu>> CUTTING_TEACH = REGISTER.register("farmers_cutting_teach", () -> IMenuTypeExtension.create(CuttingTeachMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<CookingTeachMenu>> COOKING_TEACH = REGISTER.register("farmers_cooking_teach", () -> IMenuTypeExtension.create(CookingTeachMenu::new));

	private ModuleMenuTypes()
	{

	}

}
