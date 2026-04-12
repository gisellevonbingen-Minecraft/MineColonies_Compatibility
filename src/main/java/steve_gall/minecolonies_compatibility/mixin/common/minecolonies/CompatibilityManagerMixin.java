package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import java.util.ArrayList;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;
import com.minecolonies.api.compatibility.CompatibilityManager;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import steve_gall.minecolonies_compatibility.api.common.event.DiscoverAllItemsEvent;

@Mixin(value = CompatibilityManager.class, remap = false)
public abstract class CompatibilityManagerMixin
{
	@Shadow(remap = false)
	private static ImmutableList<ItemStack> allItems;

	@Shadow(remap = false)
	private Map<ItemStorage, CreativeModeTab> creativeModeTabMap;

	@Inject(method = "discoverAllItems", remap = false, at = @At(value = "TAIL"), cancellable = false)
	private void discoverAllItems(Level level, CallbackInfo ci)
	{
		var list = new ArrayList<>(allItems);

		NeoForge.EVENT_BUS.post(new DiscoverAllItemsEvent((stack, tab) ->
		{
			var storage = new ItemStorage(stack);

			if (this.creativeModeTabMap.put(storage, tab) == null)
			{
				list.add(stack);
			}

		}));

		allItems = ImmutableList.copyOf(list);
	}

}
