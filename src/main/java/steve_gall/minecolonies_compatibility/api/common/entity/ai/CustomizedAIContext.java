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
	private final int weaponSlot;

	public CustomizedAIContext(@NotNull AbstractEntityCitizen user, @NotNull IToolType toolType, int weaponSlot)
	{
		this.user = user;
		this.toolType = toolType;
		this.weaponSlot = weaponSlot;
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

	public int getWeaponSlot()
	{
		return this.weaponSlot;
	}

	@NotNull
	public ItemStack getWeapon()
	{
		return this.getUser().getCitizenData().getInventory().getStackInSlot(this.getWeaponSlot());
	}

}
