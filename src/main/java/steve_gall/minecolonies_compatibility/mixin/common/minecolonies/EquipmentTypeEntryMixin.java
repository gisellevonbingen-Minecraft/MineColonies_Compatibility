package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.tool.CustomizedToolSystem;

@Mixin(value = EquipmentTypeEntry.class, remap = false)
public abstract class EquipmentTypeEntryMixin
{
	@Unique
	private final EquipmentTypeEntry minecolonies_compatibility$self = (EquipmentTypeEntry) (Object) this;

	@Inject(method = "checkIsEquipment", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void checkIsEquipment(ItemStack stack, CallbackInfoReturnable<Boolean> cir)
	{
		var system = CustomizedToolSystem.select(stack);

		if (system == null)
		{
			return;
		}
		else if (system.isSpecialTool(stack, this.minecolonies_compatibility$self))
		{
			cir.setReturnValue(true);
		}

	}

	@Inject(method = "getMiningLevel", remap = false, at = @At(value = "HEAD", remap = false), cancellable = true)
	private void getMiningLevel(ItemStack stack, CallbackInfoReturnable<Integer> cir)
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
			var min = 0;
			var level = system.getLevel(stack);

			if (CustomizedToolSystem.isDurabilityBasedLevel(this.minecolonies_compatibility$self))
			{
				min = 1;
			}

			cir.setReturnValue(Math.max(level, min));
		}

	}

}
