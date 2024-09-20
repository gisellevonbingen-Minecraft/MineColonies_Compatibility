package steve_gall.minecolonies_compatibility.module.common.ewewukeks_musketmod;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import ewewukek.musketmod.Config;
import ewewukek.musketmod.GunItem;
import ewewukek.musketmod.Items;
import ewewukek.musketmod.MusketItem;
import ewewukek.musketmod.PistolItem;
import ewewukek.musketmod.Sounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
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
		public boolean canMeleeAttack(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
		{
			return context.getWeapon().getItem() == Items.MUSKET_WITH_BAYONET;
		}

		@Override
		public float getMeleeAttackDamage(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
		{
			var damage = super.getMeleeAttackDamage(context, target);
			damage += (Config.bayonetDamage - 1);
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
		public boolean canMeleeAttack(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
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
	protected boolean testAmmo(ItemStack stack)
	{
		return stack.getItem() == Items.CARTRIDGE;
	}

	@Override
	protected IDeliverableObject createAmmoRequest(int minCount)
	{
		return new Cartridge(minCount);
	}

	@Override
	protected boolean isAmmoRequest(IDeliverableObject object)
	{
		return object instanceof Cartridge;
	}

	@Override
	public boolean canRangedAttack(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		var user = context.getUser();

		if (!super.canRangedAttack(context, target))
		{
			return false;
		}

		if (!this.isLoaded(user))
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
		this.setLoadingPhase(user, 0);
	}

	@Override
	protected boolean onReloading(@NotNull AbstractEntityCitizen user)
	{
		var time = this.getReloadingTime(user);
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

		return super.onReloading(user);
	}

	@Override
	protected void onReloadStopped(@NotNull AbstractEntityCitizen user, boolean complete)
	{
		super.onReloadStopped(user, complete);

		if (complete)
		{
			this.setLoaded(user, true);
		}

	}

	@Override
	public void doRangedAttack(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		var config = this.getWeaponConfig();
		var bulletMode = this.getJobConfig().bulletMode.get();

		var user = context.getUser();
		var inventory = user.getItemHandlerCitizen();
		var bulletSlot = this.getAmmoSlot(inventory);
		var weapon = context.getWeapon();
		var bullet = ItemStack.EMPTY;
		GunItem gun = null;

		if (bulletMode.canUse() && bulletSlot > -1)
		{
			gun = (GunItem) weapon.getItem();
			bullet = inventory.extractItem(bulletSlot, 1, false);
		}
		else if (bulletMode.canDefault())
		{
			var damage = config.defaultBulletDamage.apply(user, this.getPrimarySkillLevel(user));
			var dummyGun = ModuleItems.DUMMY_GUN.get();
			dummyGun.setParent((GunItem) weapon.getItem());
			dummyGun.setDamage((float) damage);
			gun = dummyGun;
			bullet = ItemStack.EMPTY.copy();
		}

		if (bullet != null)
		{
			var hand = InteractionHand.MAIN_HAND;
			var direction = gun.aimAt(user, target);
			gun.fire(user, weapon, direction, gun.smokeOffsetFor(user, hand));
			user.playSound(gun.fireSound(weapon), 3.5F, 1.0F);
		}
		else
		{
			user.playSound(SoundEvents.NOTE_BLOCK_HAT.get(), 1.0F, 1.0F);
		}

		this.setLoaded(user, false);
	}

	@Override
	protected AttackDelayConfig getAttackDealyConfig()
	{
		return this.getWeaponConfig().attackDelay;
	}

	@Override
	protected int getReloadDuration()
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
