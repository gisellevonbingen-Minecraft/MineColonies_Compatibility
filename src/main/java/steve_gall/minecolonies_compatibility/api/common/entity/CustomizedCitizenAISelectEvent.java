package steve_gall.minecolonies_compatibility.api.common.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.citizens.event.AbstractCitizenEvent;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.entity.ai.basic.AbstractEntityAIBasic;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import steve_gall.minecolonies_tweaks.api.common.util.EventBusHelper;

public class CustomizedCitizenAISelectEvent extends AbstractCitizenEvent
{
	@NotNull
	private final AbstractEntityCitizen user;
	@NotNull
	private final ICitizenData citizen;
	@NotNull
	private final AbstractEntityAIBasic<?, ?> parentAI;
	private final int weaponSlot;

	@Nullable
	private CustomizedCitizenAI selected;

	public static CustomizedCitizenAISelectEvent of(@NotNull AbstractEntityCitizen user, @NotNull AbstractEntityAIBasic<?, ?> parentAI, int weaponSlot)
	{
		return new CustomizedCitizenAISelectEvent(user, parentAI, weaponSlot);
	}

	protected CustomizedCitizenAISelectEvent(@NotNull AbstractEntityCitizen user, @NotNull AbstractEntityAIBasic<?, ?> parentAI, int weaponSlot)
	{
		super(user.getCitizenData());

		this.user = user;
		this.citizen = user.getCitizenData();
		this.parentAI = parentAI;
		this.weaponSlot = weaponSlot;

		this.selected = null;
	}

	@Nullable
	public CustomizedCitizenAI post()
	{
		EventBusHelper.postUntil(MinecraftForge.EVENT_BUS, this, e -> e.getSelected() == null);
		return this.getSelected();
	}

	public void select(@NotNull CustomizedCitizenAI ai)
	{
		this.selected = ai;
	}

	@Nullable
	public CustomizedCitizenAI getSelected()
	{
		return this.selected;
	}

	@NotNull
	public AbstractEntityCitizen getUser()
	{
		return this.user;
	}

	@Override
	@NotNull
	public ICitizenData getCitizen()
	{
		return this.citizen;
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
		return this.getCitizen().getInventory().getStackInSlot(this.getWeaponSlot());
	}

}
