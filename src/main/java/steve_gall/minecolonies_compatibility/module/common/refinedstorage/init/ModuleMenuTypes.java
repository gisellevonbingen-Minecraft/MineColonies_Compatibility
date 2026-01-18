package steve_gall.minecolonies_compatibility.module.common.refinedstorage.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.CitizenGridContainerMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.MENU, MineColoniesCompatibility.MOD_ID);

	public static final DeferredHolder<MenuType<?>, MenuType<CitizenGridContainerMenu>> CITIZEN_GRID = REGISTER.register("citizen_grid", () -> IMenuTypeExtension.create(CitizenGridContainerMenu::new));

	private ModuleMenuTypes()
	{

	}

}
