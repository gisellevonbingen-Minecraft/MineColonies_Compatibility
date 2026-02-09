package steve_gall.minecolonies_compatibility.api.common.entity.ai.guard;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.guardtype.GuardType;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.entity.pathfinding.navigation.MinecoloniesAdvancedPathNavigate;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;

public abstract class CustomizedAIGuard extends CustomizedAI
{
	@NotNull
	public abstract GuardType getGuardType();

	public boolean isNeedPrepare(@NotNull AbstractEntityCitizen user)
	{
		return false;
	}

	public void atBuildingActions(@NotNull AbstractEntityCitizen user)
	{

	}

	public void onTargetReset(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{

	}

	public void onTargetChange(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{

	}

	/**
	 *
	 * @return Whether citizen is ready to attack the target
	 */
	public boolean canAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		return true;
	}

	public void doAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{

	}

	public int getAttackDelay(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		return 40;
	}

	public double getAttackDistance(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		return 5.0D;
	}

	public double getHorizontalSearchRange(@NotNull AbstractEntityCitizen user)
	{
		return 16.0D;
	}

	public double getVerticalSearchRange(@NotNull AbstractEntityCitizen user)
	{
		return 3.0D;
	}

	public double getCombatMovementSpeed(@NotNull AbstractEntityCitizen user)
	{
		return 1.0D;
	}

	public double getJobPathSpeed(@NotNull AbstractEntityCitizen user)
	{
		var speed = this.getCombatMovementSpeed(user);
		var min = MinecoloniesAdvancedPathNavigate.MIN_SPEED_ALLOWED;
		var max = MinecoloniesAdvancedPathNavigate.MAX_SPEED_ALLOWED;
		return Mth.clamp(speed, min, max);
	}

	public int getPrimarySkillLevel(@NotNull AbstractEntityCitizen user)
	{
		return user.getCitizenData().getCitizenSkillHandler().getLevel(this.getGuardType().getPrimarySkill());
	}

	public int getSecondarySkillLevel(@NotNull AbstractEntityCitizen user)
	{
		return user.getCitizenData().getCitizenSkillHandler().getLevel(this.getGuardType().getSecondarySkill());
	}

}
