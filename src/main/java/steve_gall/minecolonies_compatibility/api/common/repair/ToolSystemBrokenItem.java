package steve_gall.minecolonies_compatibility.api.common.repair;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.core.entity.ai.workers.crafting.EntityAIWorkBlacksmith;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.tool.CustomizedToolSystem;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IDeliverableObject;

public abstract class ToolSystemBrokenItem implements IDeliverableObject
{
	private final EntityAIWorkBlacksmith ai;

	public ToolSystemBrokenItem(@Nullable EntityAIWorkBlacksmith ai)
	{
		this.ai = ai;
	}

	@Nullable
	public EntityAIWorkBlacksmith getAI()
	{
		return this.ai;
	}

	@NotNull
	public abstract CustomizedToolSystem getToolSystem();

	@Override
	public int getCount()
	{
		return 1;
	}

	@Override
	public boolean matches(@NotNull ItemStack stack)
	{
		if (this.ai == null)
		{
			return false;
		}

		var toolSystem = this.getToolSystem();
		return toolSystem.isTool(stack) && toolSystem.isBroken(stack) && this.ai.building.getBuildingLevel() >= toolSystem.getLevel(stack);
	}

}
