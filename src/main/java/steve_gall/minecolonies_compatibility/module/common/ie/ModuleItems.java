package steve_gall.minecolonies_compatibility.module.common.ie;

import blusunrize.immersiveengineering.common.items.BulletItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class ModuleItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(Registries.ITEM, MineColoniesCompatibility.MOD_ID);

	public static DeferredHolder<Item, BulletItem<?>> DEFAULT_BULLET = REGISTER.register("ie_default_bullet", () -> new BulletItem<>(DefaultBullet.INSTANCE));

	private ModuleItems()
	{

	}

}
