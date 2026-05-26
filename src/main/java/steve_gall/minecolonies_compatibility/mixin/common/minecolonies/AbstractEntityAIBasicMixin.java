package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.jobs.AbstractJob;
import com.minecolonies.core.entity.ai.workers.AbstractAISkeleton;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIBasic;
import com.minecolonies.core.util.citizenutils.CitizenItemUtils;

import net.minecraft.world.InteractionHand;
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
	private ItemStack minecolonies_compatibility$lastItem = null;

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
		this.minecolonies_compatibility$lastItem = worker.getInventoryCitizen().getStackInSlot(toolSlot);

		if (this.minecolonies_compatibility$selectedAI != null)
		{
			CitizenItemUtils.setHeldItem(this.worker, InteractionHand.MAIN_HAND, toolSlot);
			this.minecolonies_compatibility$selectedAI.onSelected(worker);
		}
		else
		{
			CitizenItemUtils.setHeldItem(this.worker, InteractionHand.MAIN_HAND, -1);
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

		var stackInSlot = this.worker.getInventoryCitizen().getStackInSlot(slot);

		if (stackInSlot == this.minecolonies_compatibility$lastItem)
		{
			return false;
		}
		else if (!ItemStackUtils.compareItemStacksIgnoreStackSize(stackInSlot, this.minecolonies_compatibility$lastItem, false, true))
		{
			return true;
		}

		return false;
	}

}
