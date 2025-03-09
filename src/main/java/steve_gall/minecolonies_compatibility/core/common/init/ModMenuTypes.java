package steve_gall.minecolonies_compatibility.core.common.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.inventory.AccessDirectionHolderMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.BucketFillingTeachMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.SmithingTeachMenu;

public class ModMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<MenuType<BucketFillingTeachMenu>> BUCKET_FILLING_TEACH = REGISTER.register("bucket_filling_teach", () -> IForgeMenuType.create(BucketFillingTeachMenu::new));
	public static final RegistryObject<MenuType<SmithingTeachMenu>> SMITHING_TEACH = REGISTER.register("smithing_teach_teach", () -> IForgeMenuType.create(SmithingTeachMenu::new));
	public static final RegistryObject<MenuType<AccessDirectionHolderMenu<?>>> ACCESS_DIRECTION_HOLDER = REGISTER.register("access_direction_holder", () -> IForgeMenuType.create(AccessDirectionHolderMenu::new));

	private ModMenuTypes()
	{

	}

}
