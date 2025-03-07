package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecolonies.api.util.ItemStackUtils;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

@Mixin(value = ItemStackUtils.class, remap = false)
public abstract class ItemStackUtilsMixin
{
	@Redirect(method = "doesItemServeAsWeapon", remap = false, at = @At(value = "INVOKE", target = "com/minecolonies/api/compatibility/Compatibility.isTinkersWeapon", remap = false))
	private static boolean doesItemServeAsWeapon(ItemStack stack)
	{
		return ModToolTypes.KNIGHT_WEAPON.getToolType().checkIsEquipment(stack);
	}

}
