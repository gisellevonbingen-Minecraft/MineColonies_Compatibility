package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.menu.ApplePressFermentingTeachMenu;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.menu.ApplePressMashingTeachMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.MENU, MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<MenuType<?>, MenuType<ApplePressMashingTeachMenu>> APPLE_PRESS_MASHING = REGISTER.register("lets_do_vinery_apple_press_mashing", () -> IMenuTypeExtension.create(ApplePressMashingTeachMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<ApplePressFermentingTeachMenu>> APPLE_PRESS_FERMENTING = REGISTER.register("lets_do_vinery_apple_press_fermenting", () -> IMenuTypeExtension.create(ApplePressFermentingTeachMenu::new));

	private ModuleMenuTypes()
	{

	}

}
