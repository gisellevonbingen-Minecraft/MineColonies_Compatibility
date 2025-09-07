package steve_gall.minecolonies_compatibility.core.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.inventory.AccessDirectionHolderMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.BucketFillingTeachMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.SmithingTeachMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.SmithingTemplateInventoryMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.StonecutterTeachMenu;

public class ModMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<MenuType<?>, MenuType<BucketFillingTeachMenu>> BUCKET_FILLING_TEACH = REGISTER.register("bucket_filling_teach", () -> IMenuTypeExtension.create(BucketFillingTeachMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<SmithingTeachMenu>> SMITHING_TEACH = REGISTER.register("smithing_teach", () -> IMenuTypeExtension.create(SmithingTeachMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<SmithingTemplateInventoryMenu>> SMITHING_TEMPLATE_INVENTORY = REGISTER.register("smithing_template_inventory", () -> IMenuTypeExtension.create(SmithingTemplateInventoryMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<AccessDirectionHolderMenu<?>>> ACCESS_DIRECTION_HOLDER = REGISTER.register("access_direction_holder", () -> IMenuTypeExtension.create(AccessDirectionHolderMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<StonecutterTeachMenu>> STONECUTTING_TEACH = REGISTER.register("stonecutting_teach_teach", () -> IMenuTypeExtension.create(StonecutterTeachMenu::new));

	private ModMenuTypes()
	{

	}

}
