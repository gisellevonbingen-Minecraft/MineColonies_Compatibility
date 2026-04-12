package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.compatibility.tinkers.TinkersToolHelper;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.constant.IToolType;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.tool.CustomizedToolSystem;

@Mixin(value = TinkersToolHelper.class, remap = false)
public abstract class TinkersToolHelperMixin
{
	@Inject(method = "isTinkersWeapon", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void isTinkersWeapon(ItemStack stack, CallbackInfoReturnable<Boolean> cir)
	{
		var system = CustomizedToolSystem.select(stack);

		if (system == null)
		{
			return;
		}
		else if (system.isSword(stack))
		{
			cir.setReturnValue(true);
		}

	}

	@Inject(method = "isTinkersTool", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void isTinkersTool(ItemStack stack, IToolType toolType, CallbackInfoReturnable<Boolean> cir)
	{
		var system = CustomizedToolSystem.select(stack);

		if (system == null)
		{
			return;
		}
		else if (system.isSpecialTool(stack, toolType))
		{
			cir.setReturnValue(true);
		}
		else
		{
			cir.setReturnValue(ItemStackUtils.isTool(stack, toolType));
		}

	}

	@Inject(method = "getToolLevel", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void getToolLevel(ItemStack stack, CallbackInfoReturnable<Integer> cir)
	{
		var system = CustomizedToolSystem.select(stack);

		if (system == null)
		{
			return;
		}
		else if (system.isBroken(stack))
		{
			cir.setReturnValue(-1);
		}
		else
		{
			cir.setReturnValue(system.getLevel(stack));
		}

	}

	@Inject(method = "getAttackDamage", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void getAttackDamage(ItemStack stack, CallbackInfoReturnable<Double> cir)
	{
		var system = CustomizedToolSystem.select(stack);

		if (system == null)
		{
			return;
		}
		else if (system.isBroken(stack))
		{
			cir.setReturnValue(0.0D);
		}
		else
		{
			cir.setReturnValue((double) system.getAttackDamage(stack));
		}

	}

}
