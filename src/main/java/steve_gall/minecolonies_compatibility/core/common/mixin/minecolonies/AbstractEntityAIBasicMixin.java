package steve_gall.minecolonies_compatibility.core.common.mixin.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.jobs.AbstractJob;
import com.minecolonies.core.entity.ai.basic.AbstractAISkeleton;
import com.minecolonies.core.entity.ai.basic.AbstractEntityAIBasic;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAIContext;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.ICustomizableEntityAI;
import steve_gall.minecolonies_compatibility.core.common.entity.AbstractEntityAIBasicExtension;

@Mixin(value = AbstractEntityAIBasic.class, remap = false)
public abstract class AbstractEntityAIBasicMixin<J extends AbstractJob<?, J>, B extends AbstractBuilding> extends AbstractAISkeleton<J> implements AbstractEntityAIBasicExtension
{
	@Shadow(remap = false)
	private int slotAt = 0;

	@Unique
	private CustomizedAI minecolonies_compatibility$selectedAI;
	@Unique
	private CustomizedAIContext minecolonies_compatibility$aiContext;

	protected AbstractEntityAIBasicMixin(@NotNull J job)
	{
		super(job);
	}

	@Override
	public void minecolonies_compatibility$onTick()
	{
		if (this instanceof ICustomizableEntityAI self)
		{
			var worker = this.worker;
			var toolSlot = CitizenHelper.getMaxLevelToolSlot(worker.getCitizenData(), self.getHandToolType());
			var context = new CustomizedAIContext(worker, (AbstractEntityAIBasic<?, ?>) (Object) this, toolSlot);
			this.minecolonies_compatibility$selectedAI = CustomizedAI.select(context);

			if (this.minecolonies_compatibility$selectedAI != null)
			{
				this.minecolonies_compatibility$aiContext = context;
				worker.getCitizenItemHandler().setHeldItem(InteractionHand.MAIN_HAND, this.minecolonies_compatibility$aiContext.getWeaponSlot());
			}
			else
			{
				this.minecolonies_compatibility$aiContext = null;
				worker.getCitizenItemHandler().removeHeldItem();
			}

		}

	}

	@Redirect(method = "dumpOneMoreSlot", remap = false, at = @At(value = "INVOKE", target = "Lcom/minecolonies/api/util/ItemStackUtils;isEmpty(Lnet/minecraft/world/item/ItemStack;)Z"))
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

	@Override
	@Nullable
	public CustomizedAI minecolonies_compatibility$getSelectedAI()
	{
		return this.minecolonies_compatibility$selectedAI;
	}

	@Override
	@Nullable
	public CustomizedAIContext minecolonies_compatibility$getAIContext()
	{
		return this.minecolonies_compatibility$aiContext;
	}

}
