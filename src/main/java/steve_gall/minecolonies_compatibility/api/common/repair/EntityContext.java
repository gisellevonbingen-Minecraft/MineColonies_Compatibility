package steve_gall.minecolonies_compatibility.api.common.repair;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.entity.ai.citizen.blacksmith.EntityAIWorkBlacksmith;

import net.minecraft.world.InteractionHand;

public class EntityContext
{
	@NotNull
	private final EntityAIWorkBlacksmith ai;
	@NotNull
	private final AbstractEntityCitizen worker;

	public EntityContext(@NotNull EntityAIWorkBlacksmith ai, @NotNull AbstractEntityCitizen worker)
	{
		this.ai = ai;
		this.worker = worker;
	}

	public void setHands(int mainHand, int offHand)
	{
		var inventory = this.worker.getInventoryCitizen();
		inventory.setHeldItem(InteractionHand.MAIN_HAND, mainHand);
		this.worker.setItemInHand(InteractionHand.MAIN_HAND, inventory.getStackInSlot(mainHand));
		this.worker.setItemInHand(InteractionHand.OFF_HAND, inventory.getStackInSlot(offHand));
	}

	@NotNull
	public EntityAIWorkBlacksmith getAI()
	{
		return this.ai;
	}

	@NotNull
	public AbstractEntityCitizen getWorker()
	{
		return this.worker;
	}

}
