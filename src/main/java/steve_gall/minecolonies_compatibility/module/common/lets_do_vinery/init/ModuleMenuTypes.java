package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.menu.ApplePressFermentingTeachMenu;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.menu.ApplePressMashingTeachMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<MenuType<ApplePressMashingTeachMenu>> APPLE_PRESS_MASHING = REGISTER.register("lets_do_vinery_apple_press", () -> IForgeMenuType.create(ApplePressMashingTeachMenu::new));
	public static final RegistryObject<MenuType<ApplePressFermentingTeachMenu>> APPLE_PRESS_FERMENTING = REGISTER.register("lets_do_vinery_apple_press_fermenting", () -> IForgeMenuType.create(ApplePressFermentingTeachMenu::new));

	private ModuleMenuTypes()
	{

	}

}
