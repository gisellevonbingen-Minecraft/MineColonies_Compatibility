package steve_gall.minecolonies_compatibility.module.common.ie;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.DamageSourceKeys;
import com.minecolonies.api.util.constant.GuardConstants;

import blusunrize.immersiveengineering.api.tool.BulletHandler.IBullet;
import blusunrize.immersiveengineering.common.items.BulletItem;
import blusunrize.immersiveengineering.common.items.RevolverItem;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAIContext;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.CustomizedAIGunner;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDelayConfig;
import steve_gall.minecolonies_compatibility.module.common.ie.IEConfig.JobConfig.GunnerRevolverConfig;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IDeliverableObject;

public class GunnerRevolverAI extends CustomizedAIGunner
{
	public static final String TAG_KEY = MineColoniesCompatibility.rl("ie_gunner_revolver").toString();

	public GunnerRevolverAI()
	{

	}

	@Override
	public boolean test(@NotNull CustomizedAIContext context)
	{
		return super.test(context) && context.getWeapon().getItem() instanceof RevolverItem;
	}

	@Override
	protected boolean testAmmo(ItemStack stack)
	{
		return stack.getItem() instanceof BulletItem;
	}

	@Override
	protected IDeliverableObject createAmmoRequest(int minCount)
	{
		return new Bullet(minCount);
	}

	@Override
	protected boolean isAmmoRequest(IDeliverableObject object)
	{
		return object instanceof Bullet;
	}

	@Override
	public boolean canAttack(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		if (!super.canAttack(context, target))
		{
			return false;
		}

		var user = context.getUser();

		if (user.distanceTo(target) <= GuardConstants.MAX_DISTANCE_FOR_ATTACK)
		{
			return false;
		}

		if (this.getWeaponConfig().needReload.get().booleanValue() && this.getBulletCount(user) <= 0)
		{
			if (!this.reload(user))
			{
				return false;
			}

		}

		return true;
	}

	@Override
	protected void onReloadStarted(@NotNull AbstractEntityCitizen user)
	{
		super.onReloadStarted(user);

		user.playSound(IESounds.revolverReload.get(), 1.0F, 1.0F);
	}

	@Override
	protected void onReloadStopped(@NotNull AbstractEntityCitizen user, boolean complete)
	{
		super.onReloadStopped(user, complete);

		if (complete)
		{
			this.setBulletCount(user, 8);
		}

	}

	@Override
	public void doAttack(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		var config = this.getWeaponConfig();
		var bulletMode = this.getJobConfig().bulletMode.get();

		var user = context.getUser();
		var inventory = user.getItemHandlerCitizen();
		var bulletSlot = this.getAmmoSlot(inventory);
		var weapon = context.getWeapon();
		var level = user.level();

		if (user.distanceTo(target) <= GuardConstants.MAX_DISTANCE_FOR_ATTACK)
		{
			var melee = RevolverItem.getUpgradeValue_d(weapon, "melee");
			var damage = 1.0F;

			if (melee != 0.0D)
			{
				damage += (float) melee;
			}

			var source = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageSourceKeys.GUARD), user);
			target.hurt(source, damage);
		}
		else
		{
			ItemStack bullet = null;
			IBullet bulletType = null;

			if (bulletMode.canUse() && bulletSlot > -1)
			{
				bullet = inventory.extractItem(bulletSlot, 1, false);
				bulletType = ((BulletItem) bullet.getItem()).getType();
			}
			else if (bulletMode.canDefault())
			{
				bullet = ItemStack.EMPTY.copy();
				bulletType = DefaultBullet.INSTANCE;
				DefaultBullet.putDamage(bullet, config.defaultBulletDamage.apply(user, this.getPrimarySkillLevel(user)));
			}

			if (bulletType != null && bullet != null)
			{
				var noise = RevolverItem.fireProjectile(level, user, weapon, bulletType, bullet);
				this.insertItem(user, inventory, bulletType.getCasing(bullet).copy());

				if (config.occurNoise.get().booleanValue())
				{
					Utils.attractEnemies(user, 64.0F * noise);

					if (noise > 0.2F)
					{
						GameEvent eventTriggered = noise > 0.5F ? GameEvent.EXPLODE : GameEvent.PROJECTILE_SHOOT;
						level.gameEvent(eventTriggered, user.position(), GameEvent.Context.of(user));
					}

				}

			}
			else
			{
				user.playSound(SoundEvents.NOTE_BLOCK_HAT.get(), 1.0F, 1.0F);
			}

			if (config.needReload.get().booleanValue())
			{
				this.setBulletCount(user, this.getBulletCount(user) - 1);
			}

		}

	}

	@Override
	protected AttackDelayConfig getAttackDealyConfig()
	{
		return this.getWeaponConfig().attackDelay;
	}

	@Override
	public double getAttackDistance(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		var weapon = context.getWeapon();
		var distance = super.getAttackDistance(context, target);

		if (weapon.getItem() instanceof RevolverItem item && item.canZoom(weapon, null))
		{
			distance *= this.getWeaponConfig().scopeRangeMultiplier.get().doubleValue();
		}

		return distance;
	}

	@Override
	public double getHorizontalSearchRange(@NotNull CustomizedAIContext context)
	{
		var weapon = context.getWeapon();
		var range = super.getHorizontalSearchRange(context);

		if (weapon.getItem() instanceof RevolverItem item && item.canZoom(weapon, null))
		{
			range *= this.getWeaponConfig().scopeRangeMultiplier.get().doubleValue();
		}

		return range;
	}

	@Override
	@NotNull
	public String getTagKey()
	{
		return TAG_KEY;
	}

	public GunnerRevolverConfig getWeaponConfig()
	{
		return MineColoniesCompatibilityConfigServer.INSTANCE.modules.IE.job.gunnerRevolver;
	}

	public int getBulletCount(@NotNull AbstractEntityCitizen user)
	{
		return this.getOrEmptyTag(user).getInt("bulletCount");
	}

	public void setBulletCount(@NotNull AbstractEntityCitizen user, int count)
	{
		this.getOrCreateTag(user).putInt("bulletCount", Math.max(count, 0));
	}

	@Override
	protected int getReloadDuration()
	{
		return this.getWeaponConfig().reloadDuration.get().intValue();
	}

}
