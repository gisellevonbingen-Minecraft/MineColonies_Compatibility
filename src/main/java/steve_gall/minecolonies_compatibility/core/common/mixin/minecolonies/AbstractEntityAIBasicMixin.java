package steve_gall.minecolonies_compatibility.core.common.mixin.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.core.entity.ai.basic.AbstractEntityAIBasic;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.entity.ICustomizableEntityAI;

@Mixin(value = AbstractEntityAIBasic.class, remap = false)
public class AbstractEntityAIBasicMixin
{
	@Shadow
	private int slotAt = 0;

	@Redirect(method = "dumpOneMoreSlot", at = @At(value = "INVOKE", target = "Lcom/minecolonies/api/util/ItemStackUtils;isEmpty(Lnet/minecraft/world/item/ItemStack;)Z"))
	private boolean dumpOneMoreSlot_isEmpty(ItemStack stackToDump)
	{
		var empty = ItemStackUtils.isEmpty(stackToDump);

		if (empty)
		{
			return true;
		}
		else if (this instanceof ICustomizableEntityAI self)
		{
			var ai = self.getSelectedAI();

			if (ai != null)
			{
				var context = self.getAIContext();

				if (this.slotAt == context.getWeaponSlot())
				{
					return true;
				}
				else if (!ai.canDump(context, this.slotAt, stackToDump))
				{
					return true;
				}

			}

		}

		return false;
	}

}
