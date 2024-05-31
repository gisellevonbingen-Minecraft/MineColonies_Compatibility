package steve_gall.minecolonies_compatibility.api.common.entity.ai.guard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.guardtype.GuardType;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.constant.GuardConstants;
import com.minecolonies.core.colony.buildings.modules.settings.GuardTaskSetting;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
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

	protected abstract boolean testBullet(ItemStack stack);

	public int getBulletSlot(@NotNull IItemHandler inventory)
	{
		for (var i = 0; i < inventory.getSlots(); i++)
		{
			if (this.testBullet(inventory.getStackInSlot(i)))
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

		if (!CitizenHelper.isRequested(citizen, CustomizableDeliverable.TYPE_TOKEN, r -> this.isBulleteRequest(r.getRequest().getObject())))
		{
			citizen.getWorkBuilding().createRequest(citizen, new CustomizableDeliverable(this.createBulletRequest(minCount)), async);
			return true;
		}
		else
		{
			return false;
		}

	}

	protected abstract IDeliverableObject createBulletRequest(int minCount);

	protected abstract boolean isBulleteRequest(IDeliverableObject object);

	public boolean takeBullets(@Nullable AbstractEntityCitizen user)
	{
		var citizen = user.getCitizenData();
		var building = citizen.getWorkBuilding();
		var inventory = citizen.getInventory();
		return InventoryUtils.transferXOfFirstSlotInProviderWithIntoNextFreeSlotInItemHandler(building, this::testBullet, 64, inventory);
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
			this.takeBullets(user);

			var minCount = 16;
			var bulletCount = InventoryUtils.getItemCountInItemHandler(citizen.getInventory(), this::testBullet);

			if (bulletCount < minCount)
			{
				this.requestBullets(user, minCount, true);
			}

		}

	}

	public boolean checkBullets(@NotNull AbstractEntityCitizen user)
	{
		var bulletMode = this.getJobConfig().bulletMode.get();

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
	public boolean canAttack(@NotNull CustomizedAIContext context, @NotNull LivingEntity target)
	{
		var user = context.getUser();

		if (user.distanceTo(target) <= GuardConstants.MAX_DISTANCE_FOR_ATTACK)
		{
			return true;
		}
		else if (!this.checkBullets(user))
		{
			return false;
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

}
