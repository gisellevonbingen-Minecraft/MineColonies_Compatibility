package steve_gall.minecolonies_compatibility.api.common.entity.guard;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.guardtype.GuardType;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import steve_gall.minecolonies_compatibility.api.common.entity.CustomizedCitizenAIAttack;

public abstract class CustomizedCitizenAIGuard extends CustomizedCitizenAIAttack
{
	@NotNull
	public abstract GuardType getGuardType();

	public int getPrimarySkillLevel(@NotNull AbstractEntityCitizen user)
	{
		return user.getCitizenData().getCitizenSkillHandler().getLevel(this.getGuardType().getPrimarySkill());
	}

	public int getSecondarySkillLevel(@NotNull AbstractEntityCitizen user)
	{
		return user.getCitizenData().getCitizenSkillHandler().getLevel(this.getGuardType().getSecondarySkill());
	}

}
