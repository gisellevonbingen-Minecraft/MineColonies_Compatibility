package steve_gall.minecolonies_compatibility.module.common.tconstruct.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.menu.RepairMaterialTeachMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<MenuType<RepairMaterialTeachMenu>> REPAIR_MATERIAL_TEACH = REGISTER.register("tconstruct_repair_material_teach", () -> IForgeMenuType.create(RepairMaterialTeachMenu::new));

	private ModuleMenuTypes()
	{

	}

}
