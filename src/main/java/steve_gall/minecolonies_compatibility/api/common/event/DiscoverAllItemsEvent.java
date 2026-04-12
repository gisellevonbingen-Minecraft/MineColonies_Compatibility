package steve_gall.minecolonies_compatibility.api.common.event;

import java.util.function.BiConsumer;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

/**
 * {@link MinecraftForge#EVENT_BUS}
 */
public class DiscoverAllItemsEvent extends Event
{
	private final BiConsumer<ItemStack, CreativeModeTab> register;

	public DiscoverAllItemsEvent(BiConsumer<ItemStack, CreativeModeTab> register)
	{
		this.register = register;
	}

	public void register(ItemStack stack, CreativeModeTab tab)
	{
		this.register.accept(stack, tab);
	}

}
