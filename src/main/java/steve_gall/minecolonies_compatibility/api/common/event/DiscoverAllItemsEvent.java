package steve_gall.minecolonies_compatibility.api.common.event;

import java.util.function.BiConsumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

/**
 * {@link NeoForge#EVENT_BUS}
 */
public class DiscoverAllItemsEvent extends Event
{
	@NotNull
	private final BiConsumer<ItemStack, CreativeModeTab> register;

	public DiscoverAllItemsEvent(@NotNull BiConsumer<ItemStack, CreativeModeTab> register)
	{
		this.register = register;
	}

	public void register(@NotNull ItemStack stack, @Nullable CreativeModeTab tab)
	{
		this.register.accept(stack, tab);
	}

}
