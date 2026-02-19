package steve_gall.minecolonies_compatibility.module.common.create.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.create.CitizenStockKeeperMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<MenuType<CitizenStockKeeperMenu>> CITIZEN_STOCK_KEEPER = REGISTER.register("citizen_stock_keeper", () -> IForgeMenuType.create(CitizenStockKeeperMenu::new));

	private ModuleMenuTypes()
	{

	}

}
