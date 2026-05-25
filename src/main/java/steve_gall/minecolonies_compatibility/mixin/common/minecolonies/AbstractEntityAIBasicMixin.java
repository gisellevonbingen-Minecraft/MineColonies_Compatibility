package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.jobs.AbstractJob;
import com.minecolonies.core.entity.ai.basic.AbstractAISkeleton;
import com.minecolonies.core.entity.ai.basic.AbstractEntityAIBasic;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAIContext;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.ICustomizableEntityAI;
import steve_gall.minecolonies_compatibility.core.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.core.common.entity.AbstractEntityAIBasicExtension;

@Mixin(value = AbstractEntityAIBasic.class, remap = false)
public abstract class AbstractEntityAIBasicMixin<J extends AbstractJob<?, J>, B extends AbstractBuilding> extends AbstractAISkeleton<J> implements AbstractEntityAIBasicExtension
{
	@Shadow(remap = false)
	private int slotAt;

	@Unique
	private CustomizedAI minecolonies_compatibility$selectedAI = null;
	@Unique
	private int minecolonies_compatibility$lastSlot = -1;
	@Unique
	private Item minecolonies_compatibility$lastItem = null;

	protected AbstractEntityAIBasicMixin(@NotNull J job)
	{
		super(job);
	}

	private void updateAI(ICustomizableEntityAI self)
	{
		var worker = this.worker;
		var toolType = self.getHandToolType();
		var toolSlot = CitizenHelper.getMaxLevelToolSlot(worker.getCitizenData(), toolType);
		var context = new CustomizedAIContext(worker, toolType, toolSlot);

		this.minecolonies_compatibility$selectedAI = toolSlot == -1 ? null : CustomizedAI.select(context);
		this.minecolonies_compatibility$lastSlot = toolSlot;
		this.minecolonies_compatibility$lastItem = worker.getInventoryCitizen().getStackInSlot(toolSlot).getItem();

		if (this.minecolonies_compatibility$selectedAI != null)
		{
			worker.getCitizenItemHandler().setHeldItem(InteractionHand.MAIN_HAND, toolSlot);
			this.minecolonies_compatibility$selectedAI.onSelected(worker);
		}
		else
		{
			worker.getCitizenItemHandler().removeHeldItem();
		}

	}

	@WrapOperation(method = "dumpOneMoreSlot", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", remap = true))
	private boolean dumpOneMoreSlot_isEmpty(ItemStack stackToDump, Operation<Boolean> operation)
	{
		if (operation.call(stackToDump))
		{
			return true;
		}
		else if (this instanceof ICustomizableEntityAI self)
		{
			var ai = self.getSelectedAI();

			if (ai != null)
			{
				if (this.slotAt == ai.getMainHandSlot(this.worker))
				{
					return true;
				}
				else if (!ai.canDump(this.worker, this.slotAt, stackToDump))
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
		if (this instanceof ICustomizableEntityAI self)
		{
			if (this.needUpdateAI())
			{
				this.updateAI(self);
			}
			else
			{
				this.worker.getCitizenItemHandler().setHeldItem(InteractionHand.MAIN_HAND, this.minecolonies_compatibility$lastSlot);
			}

			return this.minecolonies_compatibility$selectedAI;
		}
		else
		{
			return null;
		}

	}

	private boolean needUpdateAI()
	{
		var slot = this.minecolonies_compatibility$lastSlot;

		if (slot == -1)
		{
			return true;
		}
		else if (this.worker.getInventoryCitizen().getStackInSlot(slot).getItem() != this.minecolonies_compatibility$lastItem)
		{
			return true;
		}

		return false;
	}

}
