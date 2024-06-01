package steve_gall.minecolonies_compatibility.module.common.reliquary;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.registries.ForgeRegistries;
import reliquary.entities.shot.NeutralShotEntity;
import reliquary.init.ModItems;
import reliquary.init.ModSounds;
import reliquary.items.HandgunItem;
import reliquary.items.MagazineItem;
import reliquary.util.potions.XRPotionHelper;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAIContext;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.CustomizedAIGunner;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDelayConfig;
import steve_gall.minecolonies_compatibility.mixin.common.reliquary.HandgunItemAccessor;
import steve_gall.minecolonies_compatibility.module.common.reliquary.ReliquaryConfig.JobConfig.GunnerHandgunConfig;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IDeliverableObject;

public class GunnerHandgunAI extends CustomizedAIGunner
{
	public static final String TAG_KEY = MineColoniesCompatibility.rl("reliquary_gunner_handgun").toString();

	public static final String TAG_DAMAGE = MineColoniesCompatibility.rl("damage").toString();

	public GunnerHandgunAI()
	{

	}

	@Override
	public boolean test(@NotNull CustomizedAIContext context)
	{
		return super.test(context) && context.getWeapon().getItem() instanceof HandgunItem;
	}

	@Override
	@NotNull
	public String getTagKey()
	{
		return TAG_KEY;
	}

	public GunnerHandgunConfig getWeaponConfig()
	{
		return MineColoniesCompatibilityConfigServer.INSTANCE.modules.reliquary.job.gunnerHandgun;
	}

	@Override
	protected boolean testAmmo(ItemStack stack)
	{
		return stack.getItem() instanceof MagazineItem item && item != ModItems.EMPTY_MAGAZINE.get();
	}

	@Override
	protected IDeliverableObject createAmmoRequest(int minCount)
	{
		return new Magazine(minCount);
	}

	@Override
	protected boolean isAmmoRequest(IDeliverableObject object)
	{
		return object instanceof Magazine;
	}

	@Override
	protected int getAmmoMinCount()
	{
		return 2;
	}

	@Override
	protected AttackDelayConfig getAttackDealyConfig()
	{
		return this.getWeaponConfig().attackDelay;
	}

	@Override
	protected int getReloadDuration()
	{
		return this.getWeaponConfig().reloadDuration.get().intValue();
	}

	@Override
	protected boolean isNeedRequestAmmo(@NotNull AbstractEntityCitizen user)
	{
		return super.isNeedRequestAmmo(user) && this.getBulletCount(user) <= 0;
	}

	@Override
	public boolean canAttack(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		if (!super.canAttack(context, target))
		{
			return false;
		}

		var user = context.getUser();
		var inventory = user.getInventoryCitizen();
		var magazineSlot = this.getAmmoSlot(inventory);

		if (this.getBulletCount(user) <= 0 || (magazineSlot > -1 && this.getMagazineType(user).isEmpty()))
		{
			if (magazineSlot > -1)
			{
				var magazine = inventory.extractItem(magazineSlot, 1, false);
				this.setMagazineType(user, ForgeRegistries.ITEMS.getKey(magazine.getItem()).toString());
				this.setPotionEffects(user, XRPotionHelper.getPotionEffectsFromStack(magazine));
				this.insertItem(user, inventory, new ItemStack(ModItems.EMPTY_MAGAZINE.get()));
			}
			else if (this.getJobConfig().bulletMode.get().canDefault())
			{
				this.setMagazineType(user, "");
				this.setPotionEffects(user, Collections.emptyList());
			}
			else
			{
				return false;
			}

			this.setBulletCount(user, 8);

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

		user.playSound(ModSounds.HANDGUN_LOAD.get(), 0.25F, 1.0F);
	}

	@Override
	public void doAttack(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		var user = context.getUser();
		var weapon = context.getWeapon();

		var magazineType = this.getMagazineType(user);
		var magazineShotFactories = ((HandgunItemAccessor) weapon.getItem()).getMagazineShotFactories();
		HandgunItem.IShotEntityFactory shotfactory = null;

		if (magazineType.isEmpty())
		{
			shotfactory = (level, player, hand) ->
			{
				var shot = new NeutralShotEntity(level, player, hand);
				var damage = this.getWeaponConfig().defaultBulletDamage.apply(user, this.getPrimarySkillLevel(user));
				shot.getPersistentData().putInt(TAG_DAMAGE, (int) damage);
				return shot;
			};
		}
		else if (magazineShotFactories.containsKey(magazineType))
		{
			shotfactory = magazineShotFactories.get(magazineType);
		}

		if (shotfactory != null)
		{
			var level = (ServerLevel) user.level();
			var player = FakePlayerFactory.getMinecraft(level);
			player.setPos(user.position());
			player.setXRot(user.getXRot());
			player.setYRot(user.getYRot());

			var potionEffects = this.getPotionEffects(user);
			var shot = shotfactory.createShot(level, player, InteractionHand.MAIN_HAND).addPotionEffects(potionEffects);
			var motionX = -Mth.sin(player.getYRot() / 180.0F * (float) Math.PI) * Mth.cos(player.getXRot() / 180.0F * (float) Math.PI);
			var motionZ = Mth.cos(player.getYRot() / 180.0F * (float) Math.PI) * Mth.cos(player.getXRot() / 180.0F * (float) Math.PI);
			var motionY = -Mth.sin(player.getXRot() / 180.0F * (float) Math.PI);
			shot.shoot(motionX, motionY, motionZ, 1.2F, 1.0F);
			level.addFreshEntity(shot);
			user.playSound(ModSounds.HANDGUN_SHOT.get(), 0.5F, 1.2F);
		}
		else
		{
			user.playSound(SoundEvents.NOTE_BLOCK_HAT.get(), 1.0F, 1.0F);
		}

		if (!magazineType.isEmpty())
		{
			this.insertItem(user, user.getInventoryCitizen(), new ItemStack(ModItems.EMPTY_BULLET.get()));
		}

		this.setBulletCount(user, this.getBulletCount(user) - 1);
	}

	public int getBulletCount(@NotNull AbstractEntityCitizen user)
	{
		return this.getOrEmptyTag(user).getInt("bulletCount");
	}

	public void setBulletCount(@NotNull AbstractEntityCitizen user, int count)
	{
		this.getOrCreateTag(user).putInt("bulletCount", Math.max(count, 0));
	}

	public String getMagazineType(@NotNull AbstractEntityCitizen user)
	{
		return this.getOrEmptyTag(user).getString("magazineType");
	}

	public void setMagazineType(@NotNull AbstractEntityCitizen user, String magazine)
	{
		this.getOrCreateTag(user).putString("magazineType", magazine);
	}

	public void setPotionEffects(@NotNull AbstractEntityCitizen user, List<MobEffectInstance> effects)
	{
		var tag = new CompoundTag();
		XRPotionHelper.addPotionEffectsToCompoundTag(tag, effects);
		this.getOrCreateTag(user).put("potionEffects", tag);
	}

	public List<MobEffectInstance> getPotionEffects(@NotNull AbstractEntityCitizen user)
	{
		var tag = this.getOrEmptyTag(user).getCompound("potionEffects");
		return XRPotionHelper.getPotionEffectsFromCompoundTag(tag);
	}

}
