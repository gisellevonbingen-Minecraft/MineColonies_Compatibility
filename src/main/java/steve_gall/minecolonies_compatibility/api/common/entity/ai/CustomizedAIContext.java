package steve_gall.minecolonies_compatibility.api.common.entity.ai;

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

	public CustomizedAIContext(@NotNull AbstractEntityCitizen user, @NotNull AbstractEntityAIBasic<?, ?> parentAI, int weaponSlot)
	{
		this.user = user;
		this.parentAI = parentAI;
		this.weaponSlot = weaponSlot;
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
