package steve_gall.minecolonies_compatibility.module.common.refinedstorage.init;

import com.refinedmods.refinedstorage.container.factory.BlockEntityContainerFactory;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.CitizenGridBlockEntity;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.CitizenGridContainerMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<MenuType<CitizenGridContainerMenu>> CITIZEN_GRID = REGISTER.register("citizen_grid", () -> IForgeMenuType.create(new BlockEntityContainerFactory<CitizenGridContainerMenu, CitizenGridBlockEntity>((windowId, inv, blockEntity) -> new CitizenGridContainerMenu(blockEntity, inv.player, windowId))));

	private ModuleMenuTypes()
	{

	}

}
