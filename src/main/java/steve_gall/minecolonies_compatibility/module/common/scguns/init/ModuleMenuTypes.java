package steve_gall.minecolonies_compatibility.module.common.scguns.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.scguns.menu.GunBenchTeachMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<MenuType<GunBenchTeachMenu>> GUN_BENCH = REGISTER.register("scguns_gun_bench", () -> IForgeMenuType.create(GunBenchTeachMenu::new));

	private ModuleMenuTypes()
	{

	}

}
