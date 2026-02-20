package steve_gall.minecolonies_compatibility.module.common.create.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.create.CitizenStockKeeperMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.MENU, MineColoniesCompatibility.MOD_ID);

	public static final DeferredHolder<MenuType<?>, MenuType<CitizenStockKeeperMenu>> CITIZEN_STOCK_KEEPER = REGISTER.register("citizen_stock_keeper", () -> IMenuTypeExtension.create(CitizenStockKeeperMenu::new));

	private ModuleMenuTypes()
	{

	}

}
