package steve_gall.minecolonies_compatibility.module.common.ie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import blusunrize.immersiveengineering.api.tool.BulletHandler.IBullet;
import blusunrize.immersiveengineering.api.tool.upgrade.UpgradeEffect;
import blusunrize.immersiveengineering.common.items.BulletItem;
import blusunrize.immersiveengineering.common.items.RevolverItem;
import blusunrize.immersiveengineering.common.items.UpgradeableToolItem;
import blusunrize.immersiveengineering.common.register.IEDataComponents;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAIContext;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.CustomizedAIGunner;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.GunnerAmmo;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDelayConfig;
import steve_gall.minecolonies_compatibility.module.common.ie.IEConfig.JobConfig.GunnerRevolverConfig;

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
	protected boolean testAmmo(@NotNull AbstractEntityCitizen user, @NotNull ItemStack stack)
	{
		return stack.getItem() instanceof BulletItem;
	}

	@Override
	@Nullable
	protected GunnerAmmo createAmmoRequest(@NotNull AbstractEntityCitizen user, int minCount)
	{
		return new Bullet(minCount);
	}

	@Override
	protected boolean isAmmoRequest(@NotNull AbstractEntityCitizen user, @NotNull GunnerAmmo object)
	{
		return object instanceof Bullet;
	}

	@Override
	public boolean canMeleeAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		return true;
	}

	@Override
	public boolean reload(@NotNull AbstractEntityCitizen user, boolean forRangedAttack)
	{
		if (this.getWeaponConfig().needReload.get().booleanValue() && this.getBulletCount(user) <= 0)
		{
			this.startReloadTimer(user);
			return false;
		}

		return true;
	}

	@Override
	protected void onReloadTimerStarted(@NotNull AbstractEntityCitizen user)
	{
		super.onReloadTimerStarted(user);

		user.playSound(IESounds.revolverReload.value(), 1.0F, 1.0F);
	}

	@Override
	protected void onReloadTimerStopped(@NotNull AbstractEntityCitizen user, boolean complete)
	{
		super.onReloadTimerStopped(user, complete);

		if (complete)
		{
			this.setBulletCount(user, 8);
		}

	}

	@Override
	public float getMeleeAttackDamage(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		var weapon = this.getMainHandItem(user);
		var upgrades = UpgradeableToolItem.getUpgradesStatic(weapon);
		var melee = upgrades.get(UpgradeEffect.MELEE);
		var damage = super.getMeleeAttackDamage(user, target);

		if (melee != 0.0D)
		{
			damage += melee;
		}

		return damage;
	}

	@Override
	public void doRangedAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		var config = this.getWeaponConfig();
		var bulletMode = this.getBulletMode();

		var inventory = user.getItemHandlerCitizen();
		var bulletSlot = this.getAmmoSlot(user, inventory);
		var weapon = this.getMainHandItem(user);
		var level = user.level();

		ItemStack bullet = null;
		IBullet<?> bulletType = null;

		if (bulletMode.canUse() && bulletSlot > -1)
		{
			bullet = inventory.extractItem(bulletSlot, 1, false);
			bulletType = ((BulletItem<?>) bullet.getItem()).getType();
		}
		else if (bulletMode.canDefault())
		{
			bullet = new ItemStack(ModuleItems.DEFAULT_BULLET);
			bullet.set(IEDataComponents.getBulletData(DefaultBullet.INSTANCE), new DefaultBullet.Data(config.defaultBulletDamage.apply(user, this.getPrimarySkillLevel(user))));
			bulletType = DefaultBullet.INSTANCE;
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
					Holder.Reference<GameEvent> eventTriggered = noise > 0.5F ? GameEvent.EXPLODE : GameEvent.PROJECTILE_SHOOT;
					level.gameEvent(eventTriggered, user.position(), GameEvent.Context.of(user));
				}

			}

		}
		else
		{
			user.playSound(SoundEvents.NOTE_BLOCK_HAT.value(), 1.0F, 1.0F);
		}

		if (config.needReload.get().booleanValue())
		{
			this.setBulletCount(user, this.getBulletCount(user) - 1);
		}

	}

	@Override
	@Nullable
	protected AttackDelayConfig getAttackDealyConfig(AbstractEntityCitizen user)
	{
		return this.getWeaponConfig().attackDelay;
	}

	@Override
	public double getAttackDistance(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		var weapon = this.getMainHandItem(user);
		var distance = super.getAttackDistance(user, target);

		if (weapon.getItem() instanceof RevolverItem item && item.canZoom(weapon, null))
		{
			distance *= this.getWeaponConfig().scopeRangeMultiplier.get().doubleValue();
		}

		return distance;
	}

	@Override
	public double getHorizontalSearchRange(@NotNull AbstractEntityCitizen user)
	{
		var weapon = this.getMainHandItem(user);
		var range = super.getHorizontalSearchRange(user);

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
	protected int getReloadTimerDuration()
	{
		return this.getWeaponConfig().reloadDuration.get().intValue();
	}

}
