package steve_gall.minecolonies_compatibility.mixin.common.ae;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import appeng.blockentity.networking.CableBusBlockEntity;
import steve_gall.minecolonies_compatibility.module.common.ae2.CableBusBlockEntityExtension;

@Pseudo
@Mixin(value = CableBusBlockEntity.class, remap = false)
public abstract class CableBusBlockEntityMixin implements CableBusBlockEntityExtension
{
	private boolean minecolonies_compatibility$chunkUnloaded;

	@Inject(method = "onChunkUnloaded", remap = false, at = @At(value = "HEAD"), cancellable = false)
	private void onChunkUnloaded(CallbackInfo ci)
	{
		this.minecolonies_compatibility$chunkUnloaded = true;
	}

	@Override
	public boolean minecolonies_compatibility$isChunkUnloaded()
	{
		return this.minecolonies_compatibility$chunkUnloaded;
	}

}
