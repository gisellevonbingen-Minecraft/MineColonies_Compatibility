package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.ProxyMethods;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.ToolHelper;

@Mixin(value = EquipmentTypeEntry.class, remap = false)
public abstract class EquipmentTypeEntryMixin
{
	@Unique
	private final EquipmentTypeEntry minecolonies_compatibility$self = (EquipmentTypeEntry) (Object) this;

	@Inject(method = "checkIsEquipment", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void checkIsEquipment(ItemStack stack, CallbackInfoReturnable<Boolean> cir)
	{
		if (ModuleManager.TCONSTRUCT.isLoaded())
		{
			if (ProxyMethods.isSpecialTool(stack, this.minecolonies_compatibility$self))
			{
				cir.setReturnValue(true);
			}

		}

	}

	@Inject(method = "getMiningLevel", remap = false, at = @At(value = "HEAD", remap = false), cancellable = true)
	private void getMiningLevel(ItemStack stack, CallbackInfoReturnable<Integer> cir)
	{
		if (ModuleManager.TCONSTRUCT.isLoaded())
		{
			if (stack.getItem() instanceof IModifiable)
			{
				if (ToolHelper.isBroken(stack))
				{
					cir.setReturnValue(-1);
				}
				else
				{
					var tier = ToolHelper.getTier(stack);
					var min = 0;

					if (isDurabilityBasedLevel(this.minecolonies_compatibility$self))
					{
						min = 1;
					}

					cir.setReturnValue(Math.max(tier, min));
				}

			}

		}

	}

	private static boolean isDurabilityBasedLevel(EquipmentTypeEntry toolType)
	{
		return toolType == ModEquipmentTypes.bow.get()//
				|| toolType == ModEquipmentTypes.fishing_rod.get()//
				|| toolType == ModEquipmentTypes.shears.get()//
				|| toolType == ModEquipmentTypes.shield.get()//
				|| toolType == ModEquipmentTypes.flint_and_steel.get();
	}

}
