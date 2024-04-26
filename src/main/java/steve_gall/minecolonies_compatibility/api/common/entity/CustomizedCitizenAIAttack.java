package steve_gall.minecolonies_compatibility.api.common.entity;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.LivingEntity;

public abstract class CustomizedCitizenAIAttack extends CustomizedCitizenAI
{
	public void atBuildingActions(@NotNull CustomizedAIContext context)
	{

	}

	/**
	 * The target maybe not be specified.
	 *
	 * @return Whether citizen can try attack
	 */
	public boolean canTryAttack(@NotNull CustomizedAIContext context)
	{
		return true;
	}

	/**
	 * The target maybe not be specified.
	 *
	 * @return Whether citizen can try attack
	 */
	public boolean canTryMoveToAttack(@NotNull CustomizedAIContext context)
	{
		return true;
	}

	/**
	 *
	 * @return Whether citizen is ready to attack the target
	 */
	public boolean canAttack(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		return true;
	}

	public void doAttack(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{

	}

	public int getAttackDelay(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		return 40;
	}

	public double getAttackDistance(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		return 5.0D;
	}

	public int getHorizontalSearchRange(@NotNull CustomizedAIContext context)
	{
		return 16;
	}

	public int getVerticalSearchRange(@NotNull CustomizedAIContext context)
	{
		return 3;
	}

	public double getCombatMovementSpeed(@NotNull CustomizedAIContext context)
	{
		return 1.0D;
	}

}
