package steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.menu.CheeseTeachMenu;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.menu.CookingTeachMenu;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.menu.WoodcuttingTeachMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<MenuType<?>, MenuType<CheeseTeachMenu>> CHEESE_TEACH = REGISTER.register("lets_do_meadow_cheese_teach", () -> IMenuTypeExtension.create(CheeseTeachMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<CookingTeachMenu>> COOKING_TEACH = REGISTER.register("lets_do_meadow_cooking_teach", () -> IMenuTypeExtension.create(CookingTeachMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<WoodcuttingTeachMenu>> WOODCUTTING_TEACH = REGISTER.register("lets_do_meadow_woodcutting_teach", () -> IMenuTypeExtension.create(WoodcuttingTeachMenu::new));

	private ModuleMenuTypes()
	{

	}

}
