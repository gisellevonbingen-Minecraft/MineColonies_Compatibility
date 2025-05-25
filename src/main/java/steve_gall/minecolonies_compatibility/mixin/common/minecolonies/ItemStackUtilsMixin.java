package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.constant.IToolType;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.tool.CustomizedToolSystem;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

@Mixin(value = ItemStackUtils.class, remap = false)
public abstract class ItemStackUtilsMixin
{
	@Inject(method = "isTool", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private static void isTool(ItemStack itemStack, IToolType toolType, CallbackInfoReturnable<Boolean> cir)
	{
		var system = CustomizedToolSystem.select(itemStack);

		if (system == null)
		{
			return;
		}
		else if (system.isSpecialTool(itemStack, toolType))
		{
			cir.setReturnValue(true);
		}

	}

	@Inject(method = "getMiningLevel", remap = false, at = @At(value = "INVOKE", target = "com/minecolonies/api/compatibility/Compatibility.getToolLevel", remap = false), cancellable = true)
	private static void getMiningLevel(ItemStack stack, IToolType toolType, CallbackInfoReturnable<Integer> cir)
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
			var level = system.getLevel(stack);

			if (!toolType.hasVariableMaterials())
			{
				cir.setReturnValue(Math.max(level, 1));
			}

		}

	}

	@Redirect(method = "doesItemServeAsWeapon", remap = false, at = @At(value = "INVOKE", target = "com/minecolonies/api/compatibility/Compatibility.isTinkersWeapon", remap = false))
	private static boolean doesItemServeAsWeapon(ItemStack stack)
	{
		return ItemStackUtils.isTool(stack, ModToolTypes.KNIGHT_WEAPON.getToolType());
	}

}
