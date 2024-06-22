package steve_gall.minecolonies_compatibility.module.common.ae2.init;

import java.util.function.Supplier;

import appeng.menu.AEBaseMenu;
import appeng.menu.implementations.MenuTypeBuilder;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.ae2.CitizenTerminalMenu;
import steve_gall.minecolonies_compatibility.module.common.ae2.CitizenTerminalPart;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<MenuType<CitizenTerminalMenu>> CITIZEN_TERMINAL = register("citizen_terminal", () -> MenuTypeBuilder.create(CitizenTerminalMenu::new, CitizenTerminalPart.class).requirePermission(null));

	private static <MENU extends AEBaseMenu> RegistryObject<MenuType<MENU>> register(String name, Supplier<MenuTypeBuilder<MENU, ?>> builderSupplier)
	{
		return REGISTER.register(name, () -> builderSupplier.get().build(name));
	}

	private ModuleMenuTypes()
	{

	}

}
