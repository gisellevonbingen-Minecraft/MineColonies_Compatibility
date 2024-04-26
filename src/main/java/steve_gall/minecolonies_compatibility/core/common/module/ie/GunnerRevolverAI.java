package steve_gall.minecolonies_compatibility.core.common.module.ie;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.guardtype.GuardType;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.core.colony.buildings.modules.settings.GuardTaskSetting;

import blusunrize.immersiveengineering.api.tool.BulletHandler.IBullet;
import blusunrize.immersiveengineering.common.items.BulletItem;
import blusunrize.immersiveengineering.common.items.RevolverItem;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import steve_gall.minecolonies_compatibility.api.common.building.BuildingHelper;
import steve_gall.minecolonies_compatibility.api.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAIContext;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.CustomizedAIGuard;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.init.ModGuardTypes;
import steve_gall.minecolonies_compatibility.core.common.init.ModJobs;
import steve_gall.minecolonies_compatibility.core.common.module.ie.IEConfig.JobConfig.GunnerRevolverConfig;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.CustomizableDeliverable;

public class GunnerRevolverAI extends CustomizedAIGuard
{
	public static final String TAG_KEY = MineColoniesCompatibility.rl("ie_gunner_revolver").toString();

	public GunnerRevolverAI()
	{

	}

	@Override
	public boolean test(@NotNull CustomizedAIContext context)
	{
		return CitizenHelper.getJobEntry(context.getUser().getCitizenData()) == ModJobs.GUNNER.get() && context.getWeapon().getItem() instanceof RevolverItem;
	}

	public int getBulletSlot(@NotNull IItemHandler inventory)
	{
		for (var i = 0; i < inventory.getSlots(); i++)
		{
			if (inventory.getStackInSlot(i).getItem() instanceof BulletItem)
			{
				return i;
			}

		}

		return -1;
	}

	/**
	 *
	 * @param user
	 * @param minCount
	 * @param async
	 * @return Request created
	 */
	public boolean requestBullets(@NotNull AbstractEntityCitizen user, int minCount, boolean async)
	{
		var citizen = user.getCitizenData();

		if (!CitizenHelper.isRequested(citizen, CustomizableDeliverable.TYPE_TOKEN, r -> r.getRequest().getObject() instanceof Bullet))
		{
			citizen.getWorkBuilding().createRequest(citizen, new CustomizableDeliverable(new Bullet(minCount)), async);
			return true;
		}
		else
		{
			return false;
		}

	}

	public boolean takeBullets(@Nullable AbstractEntityCitizen user)
	{
		var citizen = user.getCitizenData();
		var building = citizen.getWorkBuilding();
		var inventory = citizen.getInventory();
		return InventoryUtils.transferXOfFirstSlotInProviderWithIntoNextFreeSlotInItemHandler(building, item -> item.getItem() instanceof BulletItem, 64, inventory);
	}

	@Override
	public void atBuildingActions(@NotNull CustomizedAIContext context)
	{
		super.atBuildingActions(context);

		var bulletMode = this.getConfig().bulletMode.get();

		if (bulletMode.canUse() && bulletMode.canDefault())
		{
			var user = context.getUser();
			var citizen = user.getCitizenData();
			this.takeBullets(user);

			var minCount = 16;
			var bulletCount = InventoryUtils.getItemCountInItemHandler(citizen.getInventory(), item -> item.getItem() instanceof BulletItem);

			if (bulletCount < minCount)
			{
				this.requestBullets(user, minCount, true);
			}

		}

	}

	public boolean checkBullets(@NotNull AbstractEntityCitizen user)
	{
		var bulletMode = this.getConfig().bulletMode.get();

		if (bulletMode.canUse())
		{
			if (this.getBulletSlot(user.getCitizenData().getInventory()) == -1)
			{
				var async = bulletMode.canDefault();
				this.requestBullets(user, 16, async);
				return async;
			}

		}

		return true;
	}

	@Override
	public boolean canTryAttack(@NotNull CustomizedAIContext context)
	{
		var user = context.getUser();

		if (!this.checkBullets(user))
		{
			return false;
		}
		else if (this.getConfig().needReload.get().booleanValue())
		{
			if (this.getBulletCount(user) <= 0 && !this.isReloading(user))
			{
				this.startReload(user);
				user.playSound(IESounds.revolverReload.get(), 1.0F, 1.0F);
			}

			if (this.isReloading(user))
			{
				if (this.isReloadComplete(user))
				{
					this.setBulletCount(user, 8);
					this.stopReload(user);
				}
				else
				{
					return false;
				}

			}

		}

		return super.canTryAttack(context);
	}

	@Override
	public boolean canTryMoveToAttack(@NotNull CustomizedAIContext context)
	{
		var user = context.getUser();

		if (!this.checkBullets(user))
		{
			return false;
		}

		return super.canTryMoveToAttack(context);
	}

	@Override
	public void doAttack(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		var config = this.getConfig();
		var bulletMode = config.bulletMode.get();

		var user = context.getUser();
		user.getNavigation().stop();

		var inventory = user.getItemHandlerCitizen();
		var bulletSlot = this.getBulletSlot(inventory);
		var weapon = context.getWeapon();
		var level = user.level();

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
			var casing = bulletType.getCasing(bullet).copy();

			if (!casing.isEmpty())
			{
				var result = ItemHandlerHelper.insertItem(inventory, casing, false);

				if (!result.isEmpty())
				{
					BehaviorUtils.throwItem(user, result.copy(), user.position());
				}

			}

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
			level.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.NOTE_BLOCK_HAT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
		}

		if (config.needReload.get().booleanValue())
		{
			this.setBulletCount(user, this.getBulletCount(user) - 1);
		}

	}

	@Override
	public int getAttackDelay(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		var user = context.getUser();
		return this.getConfig().attackDelay.apply(user, this.getSecondarySkillLevel(user));
	}

	@Override
	public double getAttackDistance(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		var user = context.getUser();
		return this.getConfig().attackRange.apply(user, this.getSecondarySkillLevel(user), target);
	}

	@Override
	public int getHorizontalSearchRange(@NotNull CustomizedAIContext context)
	{
		return this.getConfig().searchRange.horizontal.get().intValue();
	}

	@Override
	public int getVerticalSearchRange(@NotNull CustomizedAIContext context)
	{
		var config = this.getConfig().searchRange;
		var range = config.vertical.get().intValue();

		if (BuildingHelper.IsGuardsTask(context.getUser().getCitizenData().getWorkBuilding(), GuardTaskSetting.GUARD))
		{
			range += config.verticalBonusOnGuard.get().intValue();
		}

		return range;
	}

	@Override
	public double getCombatMovementSpeed(@NotNull CustomizedAIContext context)
	{
		var config = this.getConfig().combatMoveSpeed;
		var user = context.getUser();
		return config.apply(user, this.getPrimarySkillLevel(user));
	}

	@Override
	@NotNull
	public String getTagKey()
	{
		return TAG_KEY;
	}

	@Override
	@NotNull
	public GuardType getGuardType()
	{
		return ModGuardTypes.GUNNER.get();
	}

	public GunnerRevolverConfig getConfig()
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

	public boolean isReloadComplete(@NotNull AbstractEntityCitizen user)
	{
		var reloadDuration = this.getConfig().reloadDuration.get().longValue();
		return user.level().getGameTime() >= this.getOrEmptyTag(user).getLong("reloadStarted") + reloadDuration;
	}

	public boolean isReloading(@NotNull AbstractEntityCitizen user)
	{
		return this.getOrEmptyTag(user).getLong("reloadStarted") > 0;
	}

	public void startReload(@NotNull AbstractEntityCitizen user)
	{
		this.getOrCreateTag(user).putLong("reloadStarted", user.level().getGameTime());
	}

	public void stopReload(@NotNull AbstractEntityCitizen user)
	{
		this.getOrCreateTag(user).remove("reloadStarted");
	}

}
