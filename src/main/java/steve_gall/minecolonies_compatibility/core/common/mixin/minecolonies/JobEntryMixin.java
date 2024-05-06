package steve_gall.minecolonies_compatibility.core.common.mixin.minecolonies;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.colony.jobs.registry.JobEntry;

import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

@Mixin(value = JobEntry.class, remap = false)
public abstract class JobEntryMixin
{
	@Unique
	private Optional<String> minecolonies_compatibility$translationKey = null;

	@Inject(method = "getTranslationKey", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void getTranslationKey(CallbackInfoReturnable<String> cir)
	{
		if (this.minecolonies_compatibility$translationKey == null)
		{
			var self = (JobEntry) (Object) this;
			var key = self.getKey();

			if (key.getNamespace().equals(MineColoniesCompatibility.MOD_ID))
			{
				this.minecolonies_compatibility$translationKey = Optional.of(MineColoniesCompatibility.tl("job." + key.getPath()));
			}
			else
			{
				this.minecolonies_compatibility$translationKey = Optional.empty();
			}

		}

		this.minecolonies_compatibility$translationKey.ifPresent(cir::setReturnValue);
	}

}
