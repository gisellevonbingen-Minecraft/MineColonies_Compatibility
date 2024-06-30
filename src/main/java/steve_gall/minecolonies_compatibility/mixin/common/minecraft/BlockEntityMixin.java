package steve_gall.minecolonies_compatibility.mixin.common.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.level.block.entity.BlockEntity;
import steve_gall.minecolonies_compatibility.core.common.block.entity.BlockEntityExtension;

@Mixin(value = BlockEntity.class, remap = false)
public abstract class BlockEntityMixin implements BlockEntityExtension
{
	@Unique
	private boolean minecolonies_compatibility$unloaded;

	@Inject(method = "onChunkUnloaded", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void onChunkUnloaded(CallbackInfo ci)
	{
		this.minecolonies_compatibility$unloaded = true;
	}

	@Override
	public boolean minecolonies_compatibility$isUnloaded()
	{
		return this.minecolonies_compatibility$unloaded;
	}

}
