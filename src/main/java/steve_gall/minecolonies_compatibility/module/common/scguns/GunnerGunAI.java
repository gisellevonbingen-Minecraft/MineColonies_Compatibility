package steve_gall.minecolonies_compatibility.module.common.scguns;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.InventoryUtils;
import com.mrcrayfish.framework.api.network.LevelLocation;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.CustomizedAIGunner;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.GunnerAmmo;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDelayConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.guard.BulletMode;
import top.ribs.scguns.Config;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.common.ProjectileManager;
import top.ribs.scguns.common.ReloadType;
import top.ribs.scguns.entity.projectile.ProjectileEntity;
import top.ribs.scguns.init.ModEnchantments;
import top.ribs.scguns.item.GunItem;
import top.ribs.scguns.network.PacketHandler;
import top.ribs.scguns.network.message.S2CMessageBulletTrail;
import top.ribs.scguns.network.message.S2CMessageGunSound;
import top.ribs.scguns.util.GunEnchantmentHelper;
import top.ribs.scguns.util.GunModifierHelper;

public class GunnerGunAI extends CustomizedAIGunner
{
	public static final String TAG_KEY = MineColoniesCompatibility.rl("scguns_gun").toString();

	@Override
	public @NotNull BulletMode getBulletMode()
	{
		return BulletMode.ONLY_USE;
	}

	@Override
	public boolean testTool(@NotNull ItemStack tool)
	{
		return tool.getItem() instanceof GunItem;
	}

	@Override
	protected boolean testAmmo(@NotNull AbstractEntityCitizen user, @NotNull ItemStack stack)
	{
		var weapon = this.getMainHandItem(user);
		var gun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);

		if (gun.getReloads().getReloadType() == ReloadType.SINGLE_ITEM)
		{
			return Gun.isAmmo(stack, gun.getReloads().getReloadItem());
		}
		else
		{
			return Gun.isAmmo(stack, gun.getProjectile().getItem());
		}

	}

	@Override
	protected int getAmmoMinRequestCount(@NotNull AbstractEntityCitizen user)
	{
		var weapon = this.getMainHandItem(user);
		var gun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);

		if (gun.getReloads().getReloadType() == ReloadType.SINGLE_ITEM)
		{
			return 2;
		}
		else
		{
			return Gun.getMaxAmmo(weapon) * 2;
		}

	}

	@Override
	@Nullable
	protected GunnerAmmo createAmmoRequest(@NotNull AbstractEntityCitizen user, int minCount)
	{
		var weapon = this.getMainHandItem(user);
		var gun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
		var ammoItem = gun.getProjectile().getItem();
		var ammo = new ItemStack(ammoItem);
		var count = 0;

		if (gun.getReloads().getReloadType() == ReloadType.SINGLE_ITEM)
		{
			count = Math.max(minCount, (int) (ammo.getMaxStackSize() * 2.0D / Gun.getMaxAmmo(weapon)));
		}
		else
		{
			count = Math.max(minCount, ammo.getMaxStackSize() * 2);
		}

		return new Ammo(ammoItem, count, minCount);
	}

	@Override
	protected boolean isAmmoRequest(@NotNull AbstractEntityCitizen user, @NotNull GunnerAmmo object)
	{
		var weapon = this.getMainHandItem(user);
		var gun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);

		if (object instanceof Ammo ammo)
		{
			return gun.getProjectile().getItem().equals(ammo.getAmmoItem());
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
		var gun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);

		if (gun.getGeneral().getInfiniteAmmo())
		{
			return true;
		}

		var data = weapon.get(DataComponents.CUSTOM_DATA);

		if (data == null)
		{
			return false;
		}

		return data.copyTag().getBoolean("IgnoreAmmo");
	}

	public int getAmmoCount(ItemStack weapon)
	{
		var data = weapon.get(DataComponents.CUSTOM_DATA);

		if (data == null)
		{
			return 0;
		}

		return data.copyTag().getInt("AmmoCount");
	}

	public void setAmmoCount(ItemStack weapon, int ammoCount)
	{
		var data = weapon.get(DataComponents.CUSTOM_DATA);
		var tag = data == null ? new CompoundTag() : data.copyTag();
		tag.putInt("AmmoCount", ammoCount);
		weapon.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
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
		var isSingleItem = gun.getReloads().getReloadType() == ReloadType.SINGLE_ITEM;
		var ammoCount = this.getAmmoCount(weapon);

		if (isSingleItem && ammoCount > 0)
		{
			return true;
		}

		var ammoSlot = this.getAmmoSlot(user, inventory);

		if ((ammoCount == 0 || !forRangedAttack) && ammoSlot > -1)
		{
			var ammo = inventory.getStackInSlot(ammoSlot);
			var reloading = 0;
			var shrink = 0;

			if (isSingleItem)
			{
				reloading = Gun.getMaxAmmo(weapon);
				shrink = 1;
			}
			else
			{
				reloading = Math.min(ammo.getCount(), Gun.getMaxAmmo(weapon) - ammoCount);
				shrink = reloading;
			}

			if (reloading <= 0)
			{
				return true;
			}

			ammo.shrink(shrink);
			this.setAmmoCount(weapon, ammoCount + reloading);
			this.handleReloadByproduct(user, weapon, gun, shrink);

			var reloadEachDuration = GunEnchantmentHelper.getRealReloadSpeed(weapon);
			var reloadCount = 1;

			if (gun.getReloads().getReloadType() == ReloadType.MANUAL)
			{
				reloadCount = shrink;
			}

			var tag = this.getOrCreateTag(user);
			tag.putInt("reloadTotalDuration", reloadEachDuration * reloadCount);
			tag.putInt("reloadEachDuration", reloadEachDuration);
			tag.putInt("reloadPhase", 0);
			this.startReloadTimer(user);

			return false;
		}

		return true;
	}

	private void handleReloadByproduct(AbstractEntityCitizen user, ItemStack weapon, Gun gun, int shrink)
	{
		var item = gun.getReloads().getReloadByproduct();

		if (item == null)
		{
			return;
		}

		var inventory = user.getInventoryCitizen();

		for (var i = 0; i < shrink; i++)
		{
			if (gun.getReloads().shouldGiveByproduct(user.level().getRandom(), weapon))
			{
				InventoryUtils.addItemStackToItemHandler(inventory, new ItemStack(item));
			}

		}

	}

	@Override
	protected int getReloadTimerDuration(@NotNull AbstractEntityCitizen user)
	{
		return this.getOrCreateTag(user).getInt("reloadTotalDuration");
	}

	@Override
	protected boolean onReloadTimerRunning(@NotNull AbstractEntityCitizen user)
	{
		var weapon = this.getMainHandItem(user);
		var gun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
		var time = this.getReloadTimerElapsed(user);
		var tag = this.getOrCreateTag(user);
		var reloadEachDuration = tag.getInt("reloadEachDuration");
		var reloadPhase = tag.getInt("reloadPhase");

		if (time >= ((reloadPhase + 1) * reloadEachDuration) - (reloadEachDuration / 2))
		{
			user.playSound(SoundEvent.createFixedRangeEvent(gun.getSounds().getCock(), Config.SERVER.reloadMaxDistance.get().floatValue()), 0.8F, 1.0F);
			tag.putInt("reloadPhase", reloadPhase + 1);
		}

		return super.onReloadTimerRunning(user);
	}

	@Override
	public void doRangedAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		var weapon = this.getMainHandItem(user);
		var item = (GunItem) weapon.getItem();
		var gun = item.getModifiedGun(weapon);
		var level = (ServerLevel) user.level();

		var projectileCount = gun.getGeneral().getProjectileAmount();
		var projectileFactory = ProjectileManager.getInstance().getFactory(BuiltInRegistries.ITEM.getKey(gun.getProjectile().getItem()));
		var additionalDamage = Gun.getAdditionalDamage(weapon);
		var spawnedProjectiles = new ProjectileEntity[projectileCount];
		var prevYHeadRot = user.getYHeadRot();
		user.setYHeadRot(user.getYRot());

		this.ejectCasing(user, weapon, gun);

		for (var i = 0; i < projectileCount; i++)
		{
			var projectileEntity = projectileFactory.create(level, user, weapon, item, gun);
			projectileEntity.setWeapon(weapon);
			projectileEntity.setAdditionalDamage(additionalDamage);
			level.addFreshEntity(projectileEntity);
			spawnedProjectiles[i] = projectileEntity;
		}

		user.setYHeadRot(prevYHeadRot);

		if (gun.getProjectile().isVisible())
		{
			var spawnX = user.getX();
			var spawnY = user.getY() + 1.0;
			var spawnZ = user.getZ();
			var radius = Config.COMMON.network.projectileTrackingRange.get();
			var data = GunEnchantmentHelper.getParticle(weapon);
			var messageBulletTrail = new S2CMessageBulletTrail(spawnedProjectiles, gun.getProjectile(), user.getId(), data);
			PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> LevelLocation.create(level, spawnX, spawnY, spawnZ, radius), messageBulletTrail);
		}

		var fireSound = getFireSound(weapon, gun);

		if (fireSound != null)
		{
			var posX = user.getX();
			var posY = user.getY() + user.getEyeHeight();
			var posZ = user.getZ();
			var volume = GunModifierHelper.getFireSoundVolume(weapon);
			var pitch = 0.9F + level.random.nextFloat() * 0.2F;
			var radius = GunModifierHelper.getModifiedFireSoundRadius(weapon, Config.SERVER.gunShotMaxDistance.get());
			var muzzle = gun.getDisplay().getFlash() != null;
			var messageSound = new S2CMessageGunSound(fireSound, user.getSoundSource(), (float) posX, (float) posY, (float) posZ, volume, pitch, user.getId(), muzzle, false);
			PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> LevelLocation.create(level, posX, posY, posZ, radius), messageSound);
		}

		if (!this.isIgnoreAmmo(weapon))
		{
			@SuppressWarnings("deprecation")
			var enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.RECLAIMED, weapon);

			if (enchantmentLevel == 0 || level.random.nextInt(4 - Mth.clamp(enchantmentLevel, 1, 2)) != 0)
			{
				this.setAmmoCount(weapon, this.getAmmoCount(weapon) - 1);
			}

		}

	}

	private void ejectCasing(AbstractEntityCitizen user, ItemStack stack, Gun gun)
	{
		if (!Config.COMMON.gameplay.spawnCasings.get() || !gun.getProjectile().ejectsCasing())
		{
			return;
		}

		var casingType = gun.getProjectile().casingType;

		if (casingType == null)
		{
			return;
		}

		var casingStack = new ItemStack(BuiltInRegistries.ITEM.get(casingType));

		if (casingStack.isEmpty())
		{
			return;
		}

		var baseChance = 0.4D;
		@SuppressWarnings("deprecation")
		var enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SHELL_CATCHER, stack);
		var finalChance = baseChance + (enchantmentLevel * 0.15D);

		if (Math.random() < finalChance)
		{
			InventoryUtils.addItemStackToItemHandler(user.getInventoryCitizen(), casingStack);
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
