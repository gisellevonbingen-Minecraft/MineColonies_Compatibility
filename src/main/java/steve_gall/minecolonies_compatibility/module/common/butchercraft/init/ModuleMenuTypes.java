package steve_gall.minecolonies_compatibility.module.common.butchercraft.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.menu.GrinderTeachMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<MenuType<GrinderTeachMenu>> GRINDER_TEACH = REGISTER.register("butchercraft_grinder_teach", () -> IForgeMenuType.create(GrinderTeachMenu::new));

	private ModuleMenuTypes()
	{

	}

}
