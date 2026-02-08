package steve_gall.minecolonies_compatibility.api.common.entity.ai.guard;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.guardtype.GuardType;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.constant.GuardConstants;
import com.minecolonies.core.colony.buildings.modules.settings.GuardTaskSetting;
import com.minecolonies.core.util.NamedDamageSource;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import steve_gall.minecolonies_compatibility.core.common.building.BuildingHelper;
import steve_gall.minecolonies_compatibility.core.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDelayConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.guard.BulletMode;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.guard.GunnerConfig;
import steve_gall.minecolonies_compatibility.core.common.init.ModGuardTypes;
import steve_gall.minecolonies_compatibility.core.common.init.ModJobs;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.CustomizableDeliverable;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IDeliverableObject;

public abstract class CustomizedAIGunner extends CustomizedAIGuard
{
	@Override
	@NotNull
	public GuardType getGuardType()
	{
		return ModGuardTypes.GUNNER.get();
	}

	public GunnerConfig getJobConfig()
	{
		return MineColoniesCompatibilityConfigServer.INSTANCE.jobs.gunner;
	}

	@NotNull
	public BulletMode getBulletMode()
	{
		return this.getJobConfig().bulletMode.get();
	}

	@Override
	public @NotNull JobEntry getJobEntry()
	{
		return ModJobs.GUNNER.get();
	}

	protected abstract boolean testAmmo(@NotNull AbstractEntityCitizen user, @NotNull ItemStack stack);

	@NotNull
	protected Predicate<ItemStack> getAmmoPredicate(@NotNull AbstractEntityCitizen user)
	{
		return stack -> this.testAmmo(user, stack);
	}

	public int getAmmoSlot(@NotNull AbstractEntityCitizen user, @NotNull IItemHandler inventory)
	{
		for (var i = 0; i < inventory.getSlots(); i++)
		{
			if (this.testAmmo(user, inventory.getStackInSlot(i)))
			{
				return i;
			}

		}

		return -1;
	}

	public void insertItem(@NotNull AbstractEntityCitizen user, @NotNull IItemHandler inventory, @NotNull ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return;
		}

		var result = ItemHandlerHelper.insertItem(inventory, stack, false);

		if (!result.isEmpty())
		{
			BehaviorUtils.throwItem(user, result.copy(), user.position());
		}

	}

	/**
	 *
	 * @param user
	 * @param minCount
	 * @param async
	 * @return Request created
	 */
	public boolean requestAmmo(@NotNull AbstractEntityCitizen user, int minCount, boolean async)
	{
		var citizen = user.getCitizenData();

		if (!CitizenHelper.isRequested(citizen, CustomizableDeliverable.TYPE_TOKEN, r ->
		{
			return this.isAmmoRequest(user, r.getRequest().getObject())//
					&& async == citizen.getJob().getAsyncRequests().contains(r.getId());
		}))
		{
			var request = this.createAmmoRequest(user, minCount);

			if (request != null)
			{
				citizen.getWorkBuilding().createRequest(citizen, new CustomizableDeliverable(request), async);
				return true;
			}

		}

		return false;
	}

	public boolean requestAmmo(@NotNull AbstractEntityCitizen user, boolean spare)
	{
		if (spare)
		{
			var ammoInBuilding = InventoryUtils.getItemCountInProvider(user.getCitizenData().getWorkBuilding(), this.getAmmoPredicate(user));

			if (ammoInBuilding >= this.getAmmoMinRequestCount(user))
			{
				return false;
			}

		}

		return this.requestAmmo(user, this.getAmmoMinRequestCount(user), spare || this.getBulletMode().canDefault());
	}

	@Nullable
	protected abstract IDeliverableObject createAmmoRequest(@NotNull AbstractEntityCitizen user, int minCount);

	protected abstract boolean isAmmoRequest(@NotNull AbstractEntityCitizen user, @NotNull IDeliverableObject object);

	protected int getAmmoMinRequestCount(@NotNull AbstractEntityCitizen user)
	{
		return 64;
	}

	public boolean takeAmmo(@NotNull AbstractEntityCitizen user)
	{
		var citizen = user.getCitizenData();
		var building = citizen.getWorkBuilding();
		var inventory = citizen.getInventory();
		var takeAmount = this.getAmmoMinRequestCount(user);
		return InventoryUtils.transferXOfFirstSlotInProviderWithIntoNextFreeSlotInItemHandler(building, this.getAmmoPredicate(user), takeAmount, inventory);
	}

	@Override
	public void onSelected(@NotNull AbstractEntityCitizen user)
	{
		super.onSelected(user);

		this.checkAmmo(user);
	}

	@Override
	public void tick(@NotNull AbstractEntityCitizen user)
	{
		super.tick(user);

		if (this.isReloadTimerRunning(user))
		{
			this.onReloadTimerRunning(user);
		}

	}

	@Override
	public void atBuildingActions(@NotNull AbstractEntityCitizen user)
	{
		super.atBuildingActions(user);

		var bulletMode = this.getBulletMode();

		if (bulletMode.canUse())
		{
			var citizen = user.getCitizenData();
			this.takeAmmo(user);

			var ammoCount = InventoryUtils.getItemCountInItemHandler(citizen.getInventory(), this.getAmmoPredicate(user));
			this.requestAmmo(user, ammoCount > 0 || bulletMode.canDefault());
		}

		this.setNeedPrepare(user, false);
		this.reload(user, false);
	}

	public boolean checkAmmo(@NotNull AbstractEntityCitizen user)
	{
		var bulletMode = this.getBulletMode();

		if (bulletMode.canUse())
		{
			if (this.isNeedRequestAmmo(user))
			{
				var canDefault = bulletMode.canDefault();

				if (!canDefault)
				{
					this.setNeedPrepare(user, true);
				}

				this.requestAmmo(user, canDefault);
				return canDefault;
			}

		}

		return true;
	}

	protected boolean isNeedRequestAmmo(@NotNull AbstractEntityCitizen user)
	{
		return this.getAmmoSlot(user, user.getInventoryCitizen()) == -1;
	}

	@Override
	public void onTargetChange(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		super.onTargetChange(user, target);

		this.reload(user, false);
		this.requestAmmo(user, true);
	}

	@Override
	public void onTargetReset(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		super.onTargetReset(user, target);

		this.reload(user, false);
		this.requestAmmo(user, true);
	}

	@Override
	public final boolean canAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		if (user.distanceTo(target) <= GuardConstants.MAX_DISTANCE_FOR_ATTACK && this.canMeleeAttack(user, target))
		{
			return true;
		}
		else
		{
			return this.canRangedAttack(user, target);
		}

	}

	public boolean canMeleeAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		return false;
	}

	public abstract boolean reload(@NotNull AbstractEntityCitizen user, boolean forRangedAttack);

	public boolean canRangedAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		if (!this.checkAmmo(user))
		{
			return false;
		}

		if (this.isReloadTimerRunning(user))
		{
			if (!this.onReloadTimerRunning(user))
			{
				return false;
			}

		}

		if (!this.reload(user, true))
		{
			return false;
		}

		return true;
	}

	@Override
	public final void doAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		if (user.distanceTo(target) <= GuardConstants.MAX_DISTANCE_FOR_ATTACK && this.canMeleeAttack(user, target))
		{
			this.doMeleeAttack(user, target);
		}
		else if (this.canRangedAttack(user, target))
		{
			this.doRangedAttack(user, target);
		}

	}

	public void doMeleeAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		var damage = this.getMeleeAttackDamage(user, target);
		damage += EnchantmentHelper.getDamageBonus(this.getMainHandItem(user), target.getMobType()) / 2.5D;

		var source = new NamedDamageSource(user.getName().getString(), user);
		target.hurt(source, damage);
	}

	public float getMeleeAttackDamage(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		return 1.0F;
	}

	public abstract void doRangedAttack(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target);

	@Override
	public int getAttackDelay(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		var config = this.getAttackDealyConfig();

		if (config != null)
		{
			return config.apply(user, this.getSecondarySkillLevel(user));
		}
		else
		{
			return 0;
		}

	}

	@Nullable
	protected abstract AttackDelayConfig getAttackDealyConfig();

	@Override
	public double getAttackDistance(@NotNull AbstractEntityCitizen user, @NotNull LivingEntity target)
	{
		var config = this.getJobConfig();
		return config.attackRange.apply(user, this.getSecondarySkillLevel(user), target);
	}

	@Override
	public double getHorizontalSearchRange(@NotNull AbstractEntityCitizen user)
	{
		return this.getJobConfig().searchRange.horizontal.get().doubleValue();
	}

	@Override
	public double getVerticalSearchRange(@NotNull AbstractEntityCitizen user)
	{
		var config = this.getJobConfig().searchRange;
		var range = config.vertical.get().intValue();

		if (BuildingHelper.IsGuardsTask(user.getCitizenData().getWorkBuilding(), GuardTaskSetting.GUARD))
		{
			range += config.verticalBonusOnGuard.get().intValue();
		}

		return range;
	}

	@Override
	public double getCombatMovementSpeed(@NotNull AbstractEntityCitizen user)
	{
		var config = this.getJobConfig().combatMoveSpeed;
		return config.apply(user, this.getPrimarySkillLevel(user));
	}

	protected boolean onReloadTimerRunning(@NotNull AbstractEntityCitizen user)
	{
		if (this.isReloadTimerComplete(user))
		{
			this.stopReloadTimer(user, true);
			return true;
		}
		else
		{
			return false;
		}

	}

	protected void onReloadTimerStarted(@NotNull AbstractEntityCitizen user)
	{
		user.swing(InteractionHand.MAIN_HAND);
	}

	protected void onReloadTimerStopped(@NotNull AbstractEntityCitizen user, boolean complete)
	{
		user.swing(InteractionHand.MAIN_HAND);
	}

	protected int getReloadTimerDuration()
	{
		return 0;
	}

	protected boolean isReloadTimerComplete(@NotNull AbstractEntityCitizen user)
	{
		var reloadTime = this.getReloadTimerElapsed(user);
		var reloadDuration = this.getReloadTimerDuration();
		return reloadTime >= reloadDuration;
	}

	protected int getReloadTimerElapsed(@NotNull AbstractEntityCitizen user)
	{
		var current = user.getLevel().getGameTime();
		var started = this.getOrEmptyTag(user).getLong("reloadStarted");
		return (int) (current - started);
	}

	protected boolean isReloadTimerRunning(@NotNull AbstractEntityCitizen user)
	{
		return this.getOrEmptyTag(user).getLong("reloadStarted") > 0;
	}

	protected void startReloadTimer(@NotNull AbstractEntityCitizen user)
	{
		this.getOrCreateTag(user).putLong("reloadStarted", user.getLevel().getGameTime());

		this.onReloadTimerStarted(user);
	}

	protected void stopReloadTimer(@NotNull AbstractEntityCitizen user, boolean complete)
	{
		this.getOrCreateTag(user).remove("reloadStarted");

		this.onReloadTimerStopped(user, complete);
	}

	@Override
	public boolean isNeedPrepare(@NotNull AbstractEntityCitizen user)
	{
		return this.getOrEmptyTag(user).getBoolean("needPrepare");
	}

	protected void setNeedPrepare(@NotNull AbstractEntityCitizen user, boolean needPrepare)
	{
		this.getOrCreateTag(user).putBoolean("needPrepare", needPrepare);
	}

}
