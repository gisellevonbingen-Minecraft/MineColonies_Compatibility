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
				CitizenItemUtils.setHeldItem(this.worker, InteractionHand.MAIN_HAND, context.getWeaponSlot());
			}
			else
			{
				this.minecolonies_compatibility$aiContext = null;
				CitizenItemUtils.setHeldItem(this.worker, InteractionHand.MAIN_HAND, -1);
			}

		}

	}

	@WrapOperation(method = "dumpOneMoreSlot", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
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
