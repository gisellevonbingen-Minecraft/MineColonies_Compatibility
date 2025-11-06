package steve_gall.minecolonies_compatibility.mixin.common.minecraft;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.core.entity.citizen.EntityCitizen;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.tinkerslevellingaddon.TinkersLevellingAddonModule;

@Mixin(value = ItemStack.class, remap = true)
public abstract class ItemStackMixin
{
	@Inject(method = "hurtAndBreak", remap = true, at = @At(value = "HEAD"), cancellable = false)
	private <T extends LivingEntity> void hurtAndBreak(int damage, T entity, Consumer<T> consumer, CallbackInfo ci)
	{
		if (ModuleManager.TINKERSLEVELLINGADDON.isLoaded() && entity instanceof EntityCitizen citizen)
		{
			TinkersLevellingAddonModule.onHurtAndBreak((ItemStack) (Object) this, 1, citizen, consumer);
		}

	}

}
