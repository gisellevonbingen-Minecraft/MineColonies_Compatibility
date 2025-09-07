package steve_gall.minecolonies_compatibility.module.common.butchercraft.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.menu.GrinderTeachMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<MenuType<?>, MenuType<GrinderTeachMenu>> GRINDER_TEACH = REGISTER.register("butchercraft_grinder_teach", () -> IMenuTypeExtension.create(GrinderTeachMenu::new));

	private ModuleMenuTypes()
	{

	}

}
