package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.compatibility.Compatibility;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.constant.IToolType;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.ProxyMethods;

@Mixin(value = ItemStackUtils.class, remap = false)
public abstract class ItemStackUtilsMixin
{
	@Inject(method = "isTool", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private static void isTool(ItemStack itemStack, IToolType toolType, CallbackInfoReturnable<Boolean> cir)
	{
		if (ModuleManager.TCONSTRUCT.isLoaded())
		{
			if (ProxyMethods.isSpecialTool(itemStack, toolType))
			{
				cir.setReturnValue(true);
			}

		}

	}

	@Inject(method = "getMiningLevel", remap = false, at = @At(value = "INVOKE", target = "com/minecolonies/api/compatibility/Compatibility.getToolLevel", remap = false), cancellable = true)
	private static void getMiningLevel(ItemStack stack, IToolType toolType, CallbackInfoReturnable<Integer> cir)
	{
		if (ModuleManager.TCONSTRUCT.isLoaded())
		{
			if (Compatibility.isTinkersTool(stack, toolType))
			{
				var level = Compatibility.getToolLevel(stack);

				if (!toolType.hasVariableMaterials())
				{
					cir.setReturnValue(Math.max(level, 1));
				}

			}

		}

	}

	@Redirect(method = "doesItemServeAsWeapon", remap = false, at = @At(value = "INVOKE", target = "com/minecolonies/api/compatibility/Compatibility.isTinkersWeapon", remap = false))
	private static boolean doesItemServeAsWeapon(ItemStack stack)
	{
		return ItemStackUtils.isTool(stack, ModToolTypes.KNIGHT_WEAPON.getToolType());
	}

}
