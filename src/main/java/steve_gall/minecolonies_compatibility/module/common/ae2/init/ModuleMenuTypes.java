package steve_gall.minecolonies_compatibility.module.common.ae2.init;

import java.util.function.Supplier;

import appeng.menu.AEBaseMenu;
import appeng.menu.implementations.MenuTypeBuilder;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.ae2.CitizenTerminalMenu;
import steve_gall.minecolonies_compatibility.module.common.ae2.CitizenTerminalPart;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MineColoniesCompatibility.MOD_ID);

	public static final Supplier<MenuType<CitizenTerminalMenu>> CITIZEN_TERMINAL = register("citizen_terminal", () -> MenuTypeBuilder.create(CitizenTerminalMenu::new, CitizenTerminalPart.class));

	private static <MENU extends AEBaseMenu> Supplier<MenuType<MENU>> register(String name, Supplier<MenuTypeBuilder<MENU, ?>> builderSupplier)
	{
		var menuType = builderSupplier.get().build(MineColoniesCompatibility.MOD_ID + "_" + name);
		return () -> menuType;
	}

	private ModuleMenuTypes()
	{

	}

}
