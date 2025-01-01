package steve_gall.minecolonies_compatibility.module.common.ae2.init;

import java.util.function.Function;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartItem;
import appeng.api.parts.PartModels;
import appeng.items.parts.PartItem;
import appeng.items.parts.PartModelsHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.module.common.ae2.CitizenTerminalPart;

public class ModuleParts
{
	public static final RegistryObject<PartItem<CitizenTerminalPart>> CITIZEN_TERMINAL = createPart("citizen_terminal", CitizenTerminalPart.class, CitizenTerminalPart::new);

	private static <T extends IPart> RegistryObject<PartItem<T>> createPart(String name, Class<T> partClass, Function<IPartItem<T>, T> factory)
	{
		PartModels.registerModels(PartModelsHelper.createModels(partClass));
		return item(name, props -> new PartItem<>(props, partClass, factory));
	}

	private static <T extends Item> RegistryObject<T> item(String name, Function<Item.Properties, T> factory)
	{
		return ModuleItems.REGISTER.register(name, () -> factory.apply(new Item.Properties()));
	}

	public static void init()
	{

	}

	private ModuleParts()
	{

	}

}
