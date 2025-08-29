package steve_gall.minecolonies_compatibility.module.common.ewewukeks_musketmod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import ewewukek.musketmod.GunItem;
import ewewukek.musketmod.Items;
import ewewukek.musketmod.MusketItem;
import ewewukek.musketmod.PistolItem;
import ewewukek.musketmod.Sounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAIContext;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.CustomizedAIGunner;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDelayConfig;
import steve_gall.minecolonies_compatibility.module.common.ewewukeks_musketmod.ewewukekMusketConfig.JobConfig.GunnerGunConfig;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IDeliverableObject;

public abstract class GunnerGunAI extends CustomizedAIGunner
{
	public static class Musket extends GunnerGunAI
	{
		@Override
		public boolean test(@NotNull CustomizedAIContext context)
		{
			return super.test(context) && context.getWeapon().getItem() instanceof MusketItem;
		}

		@Override
		public boolean canMeleeAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
		{
			return this.getMainHandItem(user).getItem() == Items.MUSKET_WITH_BAYONET;
		}

		@Override
		public float getMeleeAttackDamage(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
		{
			var damage = super.getMeleeAttackDamage(user, target);
			damage += MusketItem.BAYONET_DAMAGE;
			return damage;
		}

		@Override
		public GunnerGunConfig getWeaponConfig()
		{
			return MineColoniesCompatibilityConfigServer.INSTANCE.modules.ewewukekMusket.job.gunnerMusket;
		}

	}

	public static class Pistol extends GunnerGunAI
	{
		@Override
		public boolean test(@NotNull CustomizedAIContext context)
		{
			return super.test(context) && context.getWeapon().getItem() instanceof PistolItem;
		}

		@Override
		public boolean canMeleeAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
		{
			return false;
		}

		@Override
		public GunnerGunConfig getWeaponConfig()
		{
			return MineColoniesCompatibilityConfigServer.INSTANCE.modules.ewewukekMusket.job.gunnerPistol;
		}

	}

	public static final String TAG_KEY = MineColoniesCompatibility.rl("ewewukeks_musketmod_gun").toString();

	public GunnerGunAI()
	{

	}

	@Override
	protected boolean testAmmo(@NotNull AbstractEntityCitizen user, @NotNull ItemStack stack)
	{
		return stack.getItem() == Items.CARTRIDGE;
	}

	@Override
	@Nullable
	protected IDeliverableObject createAmmoRequest(@NotNull AbstractEntityCitizen user, int minCount)
	{
		return new Cartridge(minCount);
	}

	@Override
	protected boolean isAmmoRequest(@NotNull AbstractEntityCitizen user, @NotNull IDeliverableObject object)
	{
		return object instanceof Cartridge;
	}

	@Override
	public boolean reload(@NotNull AbstractEntityCitizen user, boolean forRangedAttack)
	{
		if (!this.isLoaded(user))
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
		this.setLoadingPhase(user, 0);
	}

	@Override
	protected boolean onReloadTimerRunning(@NotNull AbstractEntityCitizen user)
	{
		var time = this.getReloadTimerElapsed(user);
		var phase = this.getLoadingPhase(user);

		if (phase == 0 && time >= GunnerGunConfig.STAGE_DURATION_1)
		{
			user.playSound(Sounds.MUSKET_LOAD_0, 0.8F, 1.0F);
			this.setLoadingPhase(user, 1);
		}
		else if (phase == 1 && time >= GunnerGunConfig.STAGE_DURATION_2)
		{
			user.playSound(Sounds.MUSKET_LOAD_1, 0.8F, 1.0F);
			this.setLoadingPhase(user, 2);
		}
		else if (phase == 2 && time >= GunnerGunConfig.STAGE_DURATION_3)
		{
			user.playSound(Sounds.MUSKET_LOAD_2, 0.8F, 1.0F);
			this.setLoadingPhase(user, 3);
		}
		else if (phase == 3 && time >= GunnerGunConfig.STAGE_DURATION_4)
		{
			user.playSound(Sounds.MUSKET_READY, 0.8F, 1.0F);
			this.setLoadingPhase(user, 4);
		}

		return super.onReloadTimerRunning(user);
	}

	@Override
	protected void onReloadTimerStopped(@NotNull AbstractEntityCitizen user, boolean complete)
	{
		super.onReloadTimerStopped(user, complete);

		if (complete)
		{
			this.setLoaded(user, true);
		}

	}

	@Override
	public void doRangedAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		if (this.getMainHandItem(user).getItem() instanceof GunItem gun)
		{
			this.doRangedAttack(user, target, gun);
		}

	}

	private void doRangedAttack(AbstractEntityCitizen user, LivingEntity target, GunItem original)
	{
		var config = this.getWeaponConfig();
		var bulletMode = this.getBulletMode();

		var inventory = user.getItemHandlerCitizen();
		var bulletSlot = this.getAmmoSlot(user, inventory);
		var bullet = ItemStack.EMPTY;
		GunItem gun = null;

		if (bulletMode.canUse() && bulletSlot > -1)
		{
			gun = original;
			bullet = inventory.extractItem(bulletSlot, 1, false);
		}
		else if (bulletMode.canDefault())
		{
			var damage = config.defaultBulletDamage.apply(user, this.getPrimarySkillLevel(user));
			var maxEnergy = MusketItem.bulletSpeed * MusketItem.bulletSpeed;
			var dummyGun = ModuleItems.DUMMY_GUN.get();
			dummyGun.setParent(original);
			dummyGun.setDamageMultiplier((float) (damage / maxEnergy));
			gun = dummyGun;
			bullet = ItemStack.EMPTY.copy();
		}

		if (bullet != null)
		{
			var front = Vec3.directionFromRotation(user.getXRot(), user.getYRot());
			var arm = HumanoidArm.RIGHT;
			var isRightHand = arm == HumanoidArm.RIGHT;
			var side = Vec3.directionFromRotation(0.0F, user.getYRot() + (isRightHand ? 90.0F : -90.0F));
			var down = Vec3.directionFromRotation(user.getXRot() + 90, user.getYRot());
			gun.fire(user, front, side.add(down).scale(0.15D));

			user.playSound(gun.fireSound(), 3.5F, 1.0F);
		}
		else
		{
			user.playSound(SoundEvents.NOTE_BLOCK_HAT, 1.0F, 1.0F);
		}

		this.setLoaded(user, false);
	}

	@Override
	@Nullable
	protected AttackDelayConfig getAttackDealyConfig()
	{
		return this.getWeaponConfig().attackDelay;
	}

	@Override
	protected int getReloadTimerDuration()
	{
		return GunnerGunConfig.RELOAD_DURATION;
	}

	@Override
	@NotNull
	public String getTagKey()
	{
		return TAG_KEY;
	}

	public abstract GunnerGunConfig getWeaponConfig();

	public boolean isLoaded(@NotNull AbstractEntityCitizen user)
	{
		return this.getOrEmptyTag(user).getBoolean("loaded");
	}

	public void setLoaded(@NotNull AbstractEntityCitizen user, boolean loaded)
	{
		this.getOrCreateTag(user).putBoolean("loaded", loaded);
	}

	public int getLoadingPhase(@NotNull AbstractEntityCitizen user)
	{
		return this.getOrEmptyTag(user).getInt("loadingPhase");
	}

	public void setLoadingPhase(@NotNull AbstractEntityCitizen user, int phase)
	{
		this.getOrCreateTag(user).putInt("loadingPhase", phase);
	}

}
