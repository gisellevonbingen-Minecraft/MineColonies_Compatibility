package steve_gall.minecolonies_compatibility.module.common.tacz;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ShootResult;
import com.tacz.guns.api.item.IAmmo;
import com.tacz.guns.api.item.IGun;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAIContext;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.CustomizedAIGunner;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDelayConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.guard.BulletMode;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IDeliverableObject;

public class GunnerGunAI extends CustomizedAIGunner
{
	public static final String TAG_KEY = MineColoniesCompatibility.rl("tacz_gun").toString();

	@Override
	public @NotNull BulletMode getBulletMode()
	{
		return BulletMode.ONLY_USE;
	}

	@Override
	public boolean test(@NotNull CustomizedAIContext context)
	{
		return super.test(context) && context.getWeapon().getItem() instanceof IGun;
	}

	@Override
	public void onSelected(@NotNull AbstractEntityCitizen user)
	{
		super.onSelected(user);

		var weapon = this.getMainHandItem(user);
		var operator = IGunOperator.fromLivingEntity(user);
		operator.draw(() -> weapon);
	}

	@Override
	protected boolean testAmmo(@NotNull AbstractEntityCitizen user, @NotNull ItemStack stack)
	{
		if (stack.getItem() instanceof IAmmo ammo)
		{
			var weapon = this.getMainHandItem(user);
			return ammo.isAmmoOfGun(weapon, stack);
		}

		return false;
	}

	@Override
	protected int getAmmoMinRequestCount(@NotNull AbstractEntityCitizen user)
	{
		var weapon = this.getMainHandItem(user);

		if (weapon.getItem() instanceof IGun gun)
		{
			var gunIndex = TimelessAPI.getCommonGunIndex(gun.getGunId(weapon)).orElse(null);

			if (gunIndex != null)
			{
				var gunData = gunIndex.getGunData();
				return gunData.getAmmoAmount() * 2;
			}

		}

		return super.getAmmoMinRequestCount(user);
	}

	@Override
	@Nullable
	protected IDeliverableObject createAmmoRequest(@NotNull AbstractEntityCitizen user, int minCount)
	{
		var weapon = this.getMainHandItem(user);

		if (weapon.getItem() instanceof IGun gun)
		{
			var gunIndex = TimelessAPI.getCommonGunIndex(gun.getGunId(weapon)).orElse(null);

			if (gunIndex != null)
			{
				var gunData = gunIndex.getGunData();
				var ammoIndex = TimelessAPI.getCommonAmmoIndex(gunData.getAmmoId()).orElse(null);
				var count = minCount;

				if (ammoIndex != null)
				{
					count = Math.max(count, ammoIndex.getStackSize() * 2);
				}

				return new Ammo(gunData.getAmmoId(), count, minCount);
			}

		}

		return null;
	}

	@Override
	protected boolean isAmmoRequest(@NotNull AbstractEntityCitizen user, @NotNull IDeliverableObject object)
	{
		var weapon = this.getMainHandItem(user);

		if (weapon.getItem() instanceof IGun gun)
		{
			var gunIndex = TimelessAPI.getCommonGunIndex(gun.getGunId(weapon)).orElse(null);

			if (gunIndex != null && object instanceof Ammo ammo)
			{
				return gunIndex.getGunData().getAmmoId().equals(ammo.getAmmoId());
			}

		}

		return false;
	}

	@Override
	protected boolean isNeedRequestAmmo(@NotNull AbstractEntityCitizen user)
	{
		var weapon = this.getMainHandItem(user);

		if (weapon.getItem() instanceof IGun gun)
		{
			if (gun.hasBulletInBarrel(weapon) || gun.getCurrentAmmoCount(weapon) > 0)
			{
				return false;
			}

		}

		return super.isNeedRequestAmmo(user);
	}

	@Override
	public boolean canRangedAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		if (!super.canRangedAttack(user, target))
		{
			return false;
		}

		return true;
	}

	@Override
	public boolean reload(@NotNull AbstractEntityCitizen user, boolean forRangedAttack)
	{
		if (!forRangedAttack)
		{
			var operator = IGunOperator.fromLivingEntity(user);
			operator.reload();
		}

		return true;
	}

	@Override
	public void doRangedAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		var operator = IGunOperator.fromLivingEntity(user);
		var weapon = this.getMainHandItem(user);

		if (!operator.getSynIsAiming())
		{
			operator.aim(true);
		}

		var result = operator.shoot(user::getXRot, user::getYRot);

		if (result == ShootResult.NOT_DRAW || result == ShootResult.NOT_GUN)
		{
			operator.draw(() -> weapon);
		}
		else if (result == ShootResult.NO_AMMO)
		{
			operator.reload();
		}
		else if (result == ShootResult.NEED_BOLT)
		{
			operator.bolt();
		}

	}

	@Override
	public void onTargetReset(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		super.onTargetReset(user, target);

		var operator = IGunOperator.fromLivingEntity(user);
		operator.aim(false);
	}

	@Override
	public void onTargetChange(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		super.onTargetChange(user, target);

		var operator = IGunOperator.fromLivingEntity(user);
		operator.aim(false);
	}

	@Override
	@Nullable
	protected AttackDelayConfig getAttackDealyConfig()
	{
		return null;
	}

	@Override
	public @NotNull String getTagKey()
	{
		return TAG_KEY;
	}

}
