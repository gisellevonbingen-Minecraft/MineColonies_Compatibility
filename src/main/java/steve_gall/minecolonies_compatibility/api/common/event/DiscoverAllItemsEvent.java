package steve_gall.minecolonies_compatibility.api.common.event;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

/**
 * {@link MinecraftForge#EVENT_BUS}
 */
public class DiscoverAllItemsEvent extends Event
{
	@NotNull
	private final Consumer<ItemStack> register;

	public DiscoverAllItemsEvent(@NotNull Consumer<ItemStack> register)
	{
		this.register = register;
	}

	public void register(@NotNull ItemStack stack)
	{
		this.register.accept(stack);
	}

}
