package steve_gall.minecolonies_compatibility.api.common.entity;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIBasic;

import net.minecraft.world.item.ItemStack;

public class CustomizedAIContext
{
	@NotNull
	private final AbstractEntityCitizen user;
	@NotNull
	private final AbstractEntityAIBasic<?, ?> parentAI;
	private final int weaponSlot;

	public CustomizedAIContext(@NotNull CustomizedCitizenAISelectEvent e)
	{
		this.user = e.getUser();
		this.parentAI = e.getParentAI();
		this.weaponSlot = e.getWeaponSlot();
	}

	@NotNull
	public AbstractEntityCitizen getUser()
	{
		return this.user;
	}

	@NotNull
	public AbstractEntityAIBasic<?, ?> getParentAI()
	{
		return this.parentAI;
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
