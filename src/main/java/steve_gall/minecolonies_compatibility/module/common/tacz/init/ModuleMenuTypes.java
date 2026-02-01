package steve_gall.minecolonies_compatibility.module.common.tacz.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.tacz.menu.GunSmithTableTeachMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<MenuType<GunSmithTableTeachMenu>> GUN_SMITH_TABLE_TEACH = REGISTER.register("tacz_gun_smith_table_teach", () -> IForgeMenuType.create(GunSmithTableTeachMenu::new));

	private ModuleMenuTypes()
	{

	}

}
