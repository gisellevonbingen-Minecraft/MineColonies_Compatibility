package steve_gall.minecolonies_compatibility.module.common.ewewukeks_musketmod;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class ModuleItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(Registries.ITEM, MineColoniesCompatibility.MOD_ID);

	public static final DeferredHolder<Item, DummyGun> DUMMY_GUN = REGISTER.register("ewewukeks_musketmod_dummy_gun", DummyGun::new);

	private ModuleItems()
	{

	}

}
