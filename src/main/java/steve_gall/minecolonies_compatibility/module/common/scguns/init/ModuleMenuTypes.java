package steve_gall.minecolonies_compatibility.module.common.scguns.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.scguns.menu.GunBenchTeachMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<MenuType<?>, MenuType<GunBenchTeachMenu>> GUN_BENCH = REGISTER.register("scguns_gun_bench", () -> IMenuTypeExtension.create(GunBenchTeachMenu::new));

	private ModuleMenuTypes()
	{

	}

}
