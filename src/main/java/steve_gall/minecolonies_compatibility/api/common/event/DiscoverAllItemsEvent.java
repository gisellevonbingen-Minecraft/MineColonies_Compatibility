package steve_gall.minecolonies_compatibility.api.common.event;

import java.util.function.BiConsumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

/**
 * {@link MinecraftForge#EVENT_BUS}
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
