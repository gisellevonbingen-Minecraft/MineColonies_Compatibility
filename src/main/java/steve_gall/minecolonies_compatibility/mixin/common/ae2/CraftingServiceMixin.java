package steve_gall.minecolonies_compatibility.mixin.common.ae2;

import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import appeng.api.networking.IGridNode;
import appeng.me.service.CraftingService;
import appeng.me.service.helpers.NetworkCraftingProviders;
import steve_gall.minecolonies_compatibility.module.common.ae2.ICraftableWatcherNode;
import steve_gall.minecolonies_compatibility.module.common.ae2.INetworkCraftingProvidersExtensions;

@Mixin(value = CraftingService.class, remap = false)
public abstract class CraftingServiceMixin
{
	@Shadow(remap = false)
	private NetworkCraftingProviders craftingProviders;

	private final Map<IGridNode, ICraftableWatcherNode> minecolonies_compatibility$craftableWatchers = new HashMap<>();

	@Inject(method = "onServerEndTick", remap = false, at = @At(value = "TAIL"))
	private void onServerEndTick(CallbackInfo ci)
	{
		var self = (CraftingService) (Object) this;
		var newOutputs = ((INetworkCraftingProvidersExtensions) this.craftingProviders).minecolonies_compatibility$getNewOutputs();

		for (var what : newOutputs)
		{
			for (var watcher : this.minecolonies_compatibility$craftableWatchers.values())
			{
				watcher.onCraftableChange(self, what);
			}

		}

	}

	@Inject(method = "removeNode", remap = false, at = @At(value = "TAIL"))
	private void removeNode(IGridNode gridNode, CallbackInfo ci)
	{
		this.minecolonies_compatibility$craftableWatchers.remove(gridNode);
	}

	@Inject(method = "addNode", remap = false, at = @At(value = "TAIL"))
	private void addNode(IGridNode gridNode, CallbackInfo ci)
	{
		var watchingNode = gridNode.getService(ICraftableWatcherNode.class);

		if (watchingNode != null)
		{
			this.minecolonies_compatibility$craftableWatchers.put(gridNode, watchingNode);
		}

	}

}
