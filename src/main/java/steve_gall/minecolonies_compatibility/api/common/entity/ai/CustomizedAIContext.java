package steve_gall.minecolonies_compatibility.api.common.entity.ai;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.world.item.ItemStack;

public class CustomizedAIContext
{
	@NotNull
	private final AbstractEntityCitizen user;
	private final int weaponSlot;

	public CustomizedAIContext(@NotNull AbstractEntityCitizen user, int weaponSlot)
	{
		this.user = user;
		this.weaponSlot = weaponSlot;
	}

	@NotNull
	public AbstractEntityCitizen getUser()
	{
		return this.user;
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
