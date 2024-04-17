package steve_gall.minecolonies_compatibility.api.common.entity.guard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.colony.jobs.AbstractJobGuard;
import com.minecolonies.core.entity.ai.citizen.guard.AbstractEntityAIGuard;

import net.minecraft.world.InteractionHand;
import steve_gall.minecolonies_compatibility.api.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.api.common.entity.CustomizedAIContext;
import steve_gall.minecolonies_compatibility.api.common.entity.CustomizedCitizenAI;
import steve_gall.minecolonies_compatibility.api.common.entity.CustomizedCitizenAISelectEvent;
import steve_gall.minecolonies_compatibility.api.common.entity.ICustomizableEntityAI;

public abstract class CustomizableEntityAIGuard<J extends AbstractJobGuard<J>, B extends AbstractBuildingGuards> extends AbstractEntityAIGuard<J, B> implements ICustomizableEntityAI
{
	private CustomizedCitizenAI selectedAI;
	private CustomizedAIContext aiContext;

	public CustomizableEntityAIGuard(@NotNull J job)
	{
		super(job);
	}

	@Override
	public void tick()
	{
		var worker = this.worker;
		var toolSlot = CitizenHelper.getMaxLevelToolSlot(worker.getCitizenData(), this.getHandToolType());
		var event = CustomizedCitizenAISelectEvent.of(worker, this, toolSlot);
		this.selectedAI = event.post();

		if (this.selectedAI != null)
		{
			this.aiContext = new CustomizedAIContext(event);
			worker.getCitizenItemHandler().setHeldItem(InteractionHand.MAIN_HAND, this.aiContext.getWeaponSlot());
		}
		else
		{
			this.aiContext = null;
			worker.getCitizenItemHandler().removeHeldItem();
		}

		super.tick();
	}

	@Override
	protected void atBuildingActions()
	{
		super.atBuildingActions();

		var ai = this.getSelectedAI();

		if (ai != null)
		{
			ai.atBuildingActions(this.getAIContext());
		}

	}

	@Override
	@NotNull
	public abstract ToolType getHandToolType();

	@Override
	@Nullable
	public CustomizedAIContext getAIContext()
	{
		return this.aiContext;
	}

	@Override
	@Nullable
	public CustomizedCitizenAI getSelectedAI()
	{
		return this.selectedAI;
	}

}
