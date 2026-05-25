package steve_gall.minecolonies_compatibility.api.common.entity.ai;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.constant.IToolType;

import net.minecraft.world.item.ItemStack;

public class CustomizedAIContext
{
	@NotNull
	private final AbstractEntityCitizen user;
	@NotNull
	private final IToolType toolType;
	private final int toolSlot;

	public CustomizedAIContext(@NotNull AbstractEntityCitizen user, @NotNull IToolType toolType, int toolSlot)
	{
		this.user = user;
		this.toolType = toolType;
		this.toolSlot = toolSlot;
	}

	@NotNull
	public AbstractEntityCitizen getUser()
	{
		return this.user;
	}

	@NotNull
	public IToolType getToolType()
	{
		return this.toolType;
	}

	public int getToolSlot()
	{
		return this.toolSlot;
	}

	@NotNull
	public ItemStack getTool()
	{
		return this.getUser().getCitizenData().getInventory().getStackInSlot(this.getToolSlot());
	}

}
