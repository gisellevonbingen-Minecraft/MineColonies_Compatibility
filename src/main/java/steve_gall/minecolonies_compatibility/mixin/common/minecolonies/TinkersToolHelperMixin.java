package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.compatibility.tinkers.TinkersToolHelper;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.constant.IToolType;

import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.tools.item.ModifiableSwordItem;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.ProxyMethods;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.TConstructToolHelper;

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

	@Inject(method = "isTinkersTool", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void isTinkersTool(ItemStack stack, IToolType toolType, CallbackInfoReturnable<Boolean> cir)
	{
		if (ModuleManager.TCONSTRUCT.isLoaded())
		{
			if (ProxyMethods.isSpecialTool(stack, toolType))
			{
				cir.setReturnValue(true);
			}
			else if (stack.getItem() instanceof IModifiable)
			{
				cir.setReturnValue(ItemStackUtils.isTool(stack, toolType));
			}

		}

	}

	@Inject(method = "getToolLevel", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void getToolLevel(ItemStack stack, CallbackInfoReturnable<Integer> cir)
	{
		if (ModuleManager.TCONSTRUCT.isLoaded())
		{
			if (TConstructToolHelper.isBroken(stack))
			{
				cir.setReturnValue(-1);
			}
			else
			{
				cir.setReturnValue(TConstructToolHelper.getTier(stack));
			}

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
