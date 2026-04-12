package steve_gall.minecolonies_compatibility.module.common.silentgear.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.silentgear.menu.RepairKitInventoryMenu;
import steve_gall.minecolonies_compatibility.module.common.silentgear.menu.RepairMaterialTeachMenu;

public class ModuleMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.MENU, MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<MenuType<?>, MenuType<RepairMaterialTeachMenu>> REPAIR_MATERIAL_TEACH = REGISTER.register("silentgear_repair_material_teach", () -> IMenuTypeExtension.create(RepairMaterialTeachMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<RepairKitInventoryMenu>> REPAIR_KIT_INVENTORY = REGISTER.register("silentgear_repair_kit_inventory", () -> IMenuTypeExtension.create(RepairKitInventoryMenu::new));

	private ModuleMenuTypes()
	{

	}

}
