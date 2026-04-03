package steve_gall.minecolonies_compatibility.mixin.common.ae2;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.stacks.AEKey;
import appeng.me.service.helpers.NetworkCraftingProviders;
import steve_gall.minecolonies_compatibility.module.common.ae2.INetworkCraftingProvidersExtensions;

@Pseudo
@Mixin(value = NetworkCraftingProviders.class, remap = false)
public abstract class NetworkCraftingProvidersMixin implements INetworkCraftingProvidersExtensions
{
	private Set<AEKey> minecolonies_compatibility$newOutputs = new HashSet<>();

	@Inject(method = "addProvider", remap = false, at = @At(value = "HEAD"))
	private void addProvider(IGridNode node, CallbackInfo ci)
	{
		var provider = node.getService(ICraftingProvider.class);

		if (provider != null)
		{
			for (var pattern : provider.getAvailablePatterns())
			{
				this.minecolonies_compatibility$newOutputs.add(pattern.getPrimaryOutput().what());
			}

		}

	}

	@Override
	public Set<AEKey> minecolonies_compatibility$getNewOutputs()
	{
		var set = new HashSet<>(this.minecolonies_compatibility$newOutputs);
		this.minecolonies_compatibility$newOutputs.clear();
		return set;
	}

}
