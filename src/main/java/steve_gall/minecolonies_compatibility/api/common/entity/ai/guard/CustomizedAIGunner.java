package steve_gall.minecolonies_compatibility.api.common.entity.ai.guard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.guardtype.GuardType;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.core.colony.buildings.modules.settings.GuardTaskSetting;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAIContext;
import steve_gall.minecolonies_compatibility.core.common.building.BuildingHelper;
import steve_gall.minecolonies_compatibility.core.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDelayConfig;
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

	@Override
	public boolean test(@NotNull CustomizedAIContext context)
	{
		return CitizenHelper.getJobEntry(context.getUser().getCitizenData()) == ModJobs.GUNNER.get();
	}

	protected abstract boolean testAmmo(ItemStack stack);

	public int getAmmoSlot(IItemHandler inventory)
	{
		for (var i = 0; i < inventory.getSlots(); i++)
		{
			if (this.testAmmo(inventory.getStackInSlot(i)))
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

		if (!CitizenHelper.isRequested(citizen, CustomizableDeliverable.TYPE_TOKEN, r -> this.isAmmoRequest(r.getRequest().getObject())))
		{
			citizen.getWorkBuilding().createRequest(citizen, new CustomizableDeliverable(this.createAmmoRequest(minCount)), async);
			return true;
		}
		else
		{
			return false;
		}

	}

	protected abstract IDeliverableObject createAmmoRequest(int minCount);

	protected abstract boolean isAmmoRequest(IDeliverableObject object);

	protected int getAmmoMinCount()
	{
		return 16;
	}

	public boolean takeAmmo(@Nullable AbstractEntityCitizen user)
	{
		var citizen = user.getCitizenData();
		var building = citizen.getWorkBuilding();
		var inventory = citizen.getInventory();
		return InventoryUtils.transferXOfFirstSlotInProviderWithIntoNextFreeSlotInItemHandler(building, this::testAmmo, 64, inventory);
	}

	@Override
	public void atBuildingActions(@NotNull CustomizedAIContext context)
	{
		super.atBuildingActions(context);

		var bulletMode = this.getJobConfig().bulletMode.get();

		if (bulletMode.canUse() && bulletMode.canDefault())
		{
			var user = context.getUser();
			var citizen = user.getCitizenData();
			this.takeAmmo(user);

			var minCount = this.getAmmoMinCount();
			var ammoCount = InventoryUtils.getItemCountInItemHandler(citizen.getInventory(), this::testAmmo);

			if (ammoCount < minCount)
			{
				this.requestAmmo(user, minCount, true);
			}

		}

	}

	public boolean checkAmmo(@NotNull AbstractEntityCitizen user)
	{
		var bulletMode = this.getJobConfig().bulletMode.get();

		if (bulletMode.canUse())
		{
			if (this.isNeedRequestAmmo(user))
			{
				var async = bulletMode.canDefault();
				this.requestAmmo(user, 16, async);
				return async;
			}

		}

		return true;
	}

	protected boolean isNeedRequestAmmo(@NotNull AbstractEntityCitizen user)
	{
		return this.getAmmoSlot(user.getInventoryCitizen()) == -1;
	}

	@Override
	public boolean canAttack(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		var user = context.getUser();

		if (!this.checkAmmo(user))
		{
			return false;
		}

		if (this.isReloading(user))
		{
			if (!this.onReloading(user))
			{
				return false;
			}

		}

		return true;
	}

	@Override
	public int getAttackDelay(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		var user = context.getUser();
		return this.getAttackDealyConfig().apply(user, this.getSecondarySkillLevel(user));
	}

	protected abstract AttackDelayConfig getAttackDealyConfig();

	@Override
	public double getAttackDistance(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		var config = this.getJobConfig();
		var user = context.getUser();
		return config.attackRange.apply(user, this.getSecondarySkillLevel(user), target);
	}

	@Override
	public double getHorizontalSearchRange(@NotNull CustomizedAIContext context)
	{
		return this.getJobConfig().searchRange.horizontal.get().doubleValue();
	}

	@Override
	public double getVerticalSearchRange(@NotNull CustomizedAIContext context)
	{
		var config = this.getJobConfig().searchRange;
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
		var config = this.getJobConfig().combatMoveSpeed;
		var user = context.getUser();
		return config.apply(user, this.getPrimarySkillLevel(user));
	}

	public boolean reload(@NotNull AbstractEntityCitizen user)
	{
		if (!this.isReloading(user))
		{
			this.startReload(user);
		}

		return this.onReloading(user);
	}

	protected boolean onReloading(@NotNull AbstractEntityCitizen user)
	{
		if (this.isReloadComplete(user))
		{
			this.stopReload(user, true);
			return true;
		}
		else
		{
			return false;
		}

	}

	protected void onReloadStarted(@NotNull AbstractEntityCitizen user)
	{
		user.swing(InteractionHand.MAIN_HAND);
	}

	protected void onReloadStopped(@NotNull AbstractEntityCitizen user, boolean complete)
	{
		user.swing(InteractionHand.MAIN_HAND);
	}

	protected int getReloadDuration()
	{
		return 0;
	}

	public boolean isReloadComplete(@NotNull AbstractEntityCitizen user)
	{
		var current = user.getLevel().getGameTime();
		var started = this.getOrEmptyTag(user).getLong("reloadStarted");
		var reloadDuration = this.getReloadDuration();
		return current >= started + reloadDuration;
	}

	public boolean isReloading(@NotNull AbstractEntityCitizen user)
	{
		return this.getOrEmptyTag(user).getLong("reloadStarted") > 0;
	}

	public void startReload(@NotNull AbstractEntityCitizen user)
	{
		this.getOrCreateTag(user).putLong("reloadStarted", user.getLevel().getGameTime());

		this.onReloadStarted(user);
	}

	public void stopReload(@NotNull AbstractEntityCitizen user, boolean complete)
	{
		this.getOrCreateTag(user).remove("reloadStarted");

		this.onReloadStopped(user, complete);
	}

}
