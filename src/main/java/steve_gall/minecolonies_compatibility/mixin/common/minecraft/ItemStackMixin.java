package steve_gall.minecolonies_compatibility.mixin.common.minecraft;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin(value = ItemStack.class, remap = true)
public abstract class ItemStackMixin
{
	@Inject(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V", remap = true, at = @At(value = "HEAD"), cancellable = false)
	private void hurtAndBreak(int damage, ServerLevel level, LivingEntity entity, Consumer<Item> consumer, CallbackInfo ci)
	{
		// TODO: ModuleManager.TINKERSLEVELLINGADDON
		// if (ModuleManager.TINKERSLEVELLINGADDON.isLoaded() && entity instanceof EntityCitizen citizen)
		// {
		// TinkersLevellingAddonModule.onHurtAndBreak((ItemStack) (Object) this, damage, citizen, consumer);
		// }

	}

}
