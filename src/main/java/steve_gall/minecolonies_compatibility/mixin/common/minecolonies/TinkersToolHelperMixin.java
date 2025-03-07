package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.compatibility.tinkers.TinkersToolHelper;

import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.tools.item.ModifiableSwordItem;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.ProxyMethods;

@Mixin(value = TinkersToolHelper.class, remap = false)
public abstract class TinkersToolHelperMixin
{
	@Inject(method = "isTinkersWeapon", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void isTinkersWeapon(ItemStack stack, CallbackInfoReturnable<Boolean> cir)
	{
		if (ModuleManager.TCONSTRUCT.isLoaded())
		{
			cir.setReturnValue(stack.getItem() instanceof ModifiableSwordItem);
		}

	}

	@Inject(method = "getAttackDamage", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void getAttackDamage(ItemStack stack, CallbackInfoReturnable<Double> cir)
	{
		if (ModuleManager.TCONSTRUCT.isLoaded())
		{
			cir.setReturnValue(ProxyMethods.getAttackDamage(stack));
		}

	}

}
