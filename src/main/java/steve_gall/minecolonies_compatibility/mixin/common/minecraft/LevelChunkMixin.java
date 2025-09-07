package steve_gall.minecolonies_compatibility.mixin.common.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.level.chunk.LevelChunk;
import steve_gall.minecolonies_compatibility.core.common.world.LevelChunkExtension;

@Mixin(value = LevelChunk.class, remap = true)
public abstract class LevelChunkMixin implements LevelChunkExtension
{
	@Unique
	private boolean minecolonies_compatibility$unloaded;

	@Inject(method = "clearAllBlockEntities", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void clearAllBlockEntities(CallbackInfo ci)
	{
		this.minecolonies_compatibility$unloaded = true;
	}

	@Override
	public boolean minecolonies_compatibility$isUnloaded()
	{
		return this.minecolonies_compatibility$unloaded;
	}

}
