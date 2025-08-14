package steve_gall.minecolonies_compatibility.mixin.common.refinedstorage;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.refinedmods.refinedstorage.apiimpl.autocrafting.CraftingManager;

import steve_gall.minecolonies_compatibility.module.common.refinedstorage.ICraftingManagerExtension;

@Mixin(value = CraftingManager.class, remap = false)
public abstract class CraftingManagerMixin implements ICraftingManagerExtension
{
	private final List<Runnable> invalidateListeners = new ArrayList<>();

	@Override
	public boolean minecolonies_compatibility$addInvalidateListener(Runnable listener)
	{
		return this.invalidateListeners.add(listener);
	}

	@Override
	public boolean minecolonies_compatibility$removeInvalidateListener(Runnable listener)
	{
		return this.invalidateListeners.remove(listener);
	}

	@Inject(method = "invalidate", remap = false, at = @At(value = "TAIL"))
	private void invalidate(CallbackInfo ci)
	{
		this.invalidateListeners.forEach(Runnable::run);
	}

}
