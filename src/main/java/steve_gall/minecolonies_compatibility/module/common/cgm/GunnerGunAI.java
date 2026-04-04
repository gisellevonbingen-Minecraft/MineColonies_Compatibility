package steve_gall.minecolonies_compatibility.module.common.cgm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.mrcrayfish.framework.api.network.LevelLocation;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.ProjectileManager;
import com.mrcrayfish.guns.entity.ProjectileEntity;
import com.mrcrayfish.guns.init.ModEnchantments;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.S2CMessageBulletTrail;
import com.mrcrayfish.guns.network.message.S2CMessageGunSound;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.CustomizedAIGunner;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.GunnerAmmo;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDelayConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.guard.BulletMode;

public class GunnerGunAI extends CustomizedAIGunner
{
	public static final String TAG_KEY = MineColoniesCompatibility.rl("cgm_gun").toString();

	@Override
	public @NotNull BulletMode getBulletMode()
	{
		return BulletMode.ONLY_USE;
	}

	@Override
	public boolean testWeapon(@NotNull ItemStack weapon)
	{
		return weapon.getItem() instanceof GunItem;
	}

	@Override
	protected boolean testAmmo(@NotNull AbstractEntityCitizen user, @NotNull ItemStack stack)
	{
		var weapon = this.getMainHandItem(user);
		var gun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
		return Gun.isAmmo(stack, gun.getProjectile().getItem());
	}

	@Override
	protected int getAmmoMinRequestCount(@NotNull AbstractEntityCitizen user)
	{
		var weapon = this.getMainHandItem(user);
		var gun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
		return gun.getGeneral().getMaxAmmo() * 2;
	}

	@Override
	@Nullable
	protected GunnerAmmo createAmmoRequest(@NotNull AbstractEntityCitizen user, int minCount)
	{
		var weapon = this.getMainHandItem(user);
		var gun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
		var ammoId = gun.getProjectile().getItem();
		var ammo = new ItemStack(ForgeRegistries.ITEMS.getValue(ammoId));

		var count = Math.max(minCount, ammo.getMaxStackSize() * 2);
		return new Ammo(ammoId, count, minCount);
	}

	@Override
	protected boolean isAmmoRequest(@NotNull AbstractEntityCitizen user, @NotNull GunnerAmmo object)
	{
		var weapon = this.getMainHandItem(user);
		var gun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);

		if (object instanceof Ammo ammo)
		{
			return gun.getProjectile().getItem().equals(ammo.getAmmoId());
		}

		return false;
	}

	@Override
	protected boolean isNeedRequestAmmo(@NotNull AbstractEntityCitizen user)
	{
		var weapon = this.getMainHandItem(user);

		if (!this.isIgnoreAmmo(weapon) && this.getAmmoCount(weapon) > 0)
		{
			return false;
		}

		return super.isNeedRequestAmmo(user);
	}

	public boolean isIgnoreAmmo(ItemStack weapon)
	{
		var tag = weapon.getTag();

		if (tag == null)
		{
			return false;
		}

		return tag.getBoolean("IgnoreAmmo");
	}

	public int getAmmoCount(ItemStack weapon)
	{
		var tag = weapon.getTag();

		if (tag == null)
		{
			return 0;
		}

		return tag.getInt("AmmoCount");
	}

	public void setAmmoCount(ItemStack weapon, int ammoCount)
	{
		var tag = weapon.getOrCreateTag();
		tag.putInt("AmmoCount", ammoCount);
	}

	@Override
	public boolean reload(@NotNull AbstractEntityCitizen user, boolean forRangedAttack)
	{
		var weapon = this.getMainHandItem(user);
		var gun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);

		if (this.isIgnoreAmmo(weapon))
		{
			return true;
		}

		var inventory = user.getInventoryCitizen();
		var ammoSlot = this.getAmmoSlot(user, inventory);
		var ammoCount = this.getAmmoCount(weapon);

		if ((ammoCount == 0 || !forRangedAttack) && ammoSlot > -1)
		{
			var ammo = inventory.getStackInSlot(ammoSlot);
			var reloading = Math.min(ammo.getCount(), gun.getGeneral().getMaxAmmo() - ammoCount);
			
			if (reloading <= 0)
			{
				return true;
			}
			
			ammo.shrink(reloading);
			this.setAmmoCount(weapon, ammoCount + reloading);

			user.playSound(new SoundEvent(gun.getSounds().getCock(), Config.SERVER.reloadMaxDistance.get().floatValue()), 0.8F, 1.0F);
			this.startReloadTimer(user);
			return false;
		}

		return true;
	}

	@Override
	public void doRangedAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		var weapon = this.getMainHandItem(user);
		var item = (GunItem) weapon.getItem();
		var gun = item.getModifiedGun(weapon);

		var projectileCount = gun.getGeneral().getProjectileAmount();
		var projectileFactory = ProjectileManager.getInstance().getFactory(gun.getProjectile().getItem());
		var additionalDamage = Gun.getAdditionalDamage(weapon);
		var spawnedProjectiles = new ProjectileEntity[projectileCount];
		var prevYHeadRot = user.getYHeadRot();
		user.setYHeadRot(user.getYRot());

		for (var i = 0; i < projectileCount; i++)
		{
			var projectileEntity = projectileFactory.create(user.level, user, weapon, item, gun);
			projectileEntity.setWeapon(weapon);
			projectileEntity.setAdditionalDamage(additionalDamage);
			user.level.addFreshEntity(projectileEntity);
			spawnedProjectiles[i] = projectileEntity;
		}

		user.setYHeadRot(prevYHeadRot);

		if (!gun.getProjectile().isVisible())
		{
			var spawnX = user.getX();
			var spawnY = user.getY() + 1.0;
			var spawnZ = user.getZ();
			var radius = Config.COMMON.network.projectileTrackingRange.get();
			var data = GunEnchantmentHelper.getParticle(weapon);
			var messageBulletTrail = new S2CMessageBulletTrail(spawnedProjectiles, gun.getProjectile(), user.getId(), data);
			PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> LevelLocation.create(user.level, spawnX, spawnY, spawnZ, radius), messageBulletTrail);
		}

		var fireSound = getFireSound(weapon, gun);

		if (fireSound != null)
		{
			var posX = user.getX();
			var posY = user.getY() + user.getEyeHeight();
			var posZ = user.getZ();
			var volume = GunModifierHelper.getFireSoundVolume(weapon);
			var pitch = 0.9F + user.level.random.nextFloat() * 0.2F;
			var radius = GunModifierHelper.getModifiedFireSoundRadius(weapon, Config.SERVER.gunShotMaxDistance.get());
			var muzzle = gun.getDisplay().getFlash() != null;
			var messageSound = new S2CMessageGunSound(fireSound, user.getSoundSource(), (float) posX, (float) posY, (float) posZ, volume, pitch, user.getId(), muzzle, false);
			PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> LevelLocation.create(user.level, posX, posY, posZ, radius), messageSound);
		}

		if (!this.isIgnoreAmmo(weapon))
		{
			@SuppressWarnings("deprecation")
			var level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.RECLAIMED.get(), weapon);

			if (level == 0 || user.level.random.nextInt(4 - Mth.clamp(level, 1, 2)) != 0)
			{
				this.setAmmoCount(weapon, this.getAmmoCount(weapon) - 1);
			}

		}

	}

	private static ResourceLocation getFireSound(ItemStack stack, Gun gun)
	{
		ResourceLocation fireSound = null;

		if (GunModifierHelper.isSilencedFire(stack))
		{
			fireSound = gun.getSounds().getSilencedFire();
		}
		else if (stack.isEnchanted())
		{
			fireSound = gun.getSounds().getEnchantedFire();
		}

		if (fireSound != null)
		{
			return fireSound;
		}

		return gun.getSounds().getFire();
	}

	@Override
	public int getAttackDelay(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		var weapon = this.getMainHandItem(user);
		var item = (GunItem) weapon.getItem();
		var gun = item.getModifiedGun(weapon);

		var rate = GunEnchantmentHelper.getRate(weapon, gun);
		return GunModifierHelper.getModifiedRate(weapon, rate);
	}

	@Override
	@Nullable
	protected AttackDelayConfig getAttackDealyConfig(AbstractEntityCitizen user)
	{
		return null;
	}

	@Override
	public @NotNull String getTagKey()
	{
		return TAG_KEY;
	}

}
