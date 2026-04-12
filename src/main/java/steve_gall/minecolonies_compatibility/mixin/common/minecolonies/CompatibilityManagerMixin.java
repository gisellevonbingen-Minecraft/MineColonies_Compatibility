package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import java.util.ArrayList;
import java.util.HashSet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.minecolonies.api.compatibility.CompatibilityManager;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import steve_gall.minecolonies_compatibility.api.common.event.DiscoverAllItemsEvent;

@Mixin(value = CompatibilityManager.class, remap = false)
public abstract class CompatibilityManagerMixin
{
	@Shadow(remap = false)
	private static ImmutableList<ItemStack> allItems;

	@Shadow(remap = false)
	private static ImmutableSet<ItemStorage> allItemsSet;

	@Inject(method = "discoverAllItems", remap = false, at = @At(value = "TAIL"), cancellable = false)
	private void discoverAllItems(CallbackInfo ci)
	{
		var list = new ArrayList<>(allItems);
		var set = new HashSet<>(allItemsSet);

		MinecraftForge.EVENT_BUS.post(new DiscoverAllItemsEvent(stack ->
		{
			var storage = new ItemStorage(stack, true);

			if (set.add(storage))
			{
				list.add(stack);
			}

		}));

		allItems = ImmutableList.copyOf(list);
		allItemsSet = ImmutableSet.copyOf(set);
	}

}
