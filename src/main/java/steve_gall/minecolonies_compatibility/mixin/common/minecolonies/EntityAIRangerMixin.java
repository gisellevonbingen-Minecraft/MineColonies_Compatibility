package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.api.research.util.ResearchConstants;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.colony.jobs.JobRanger;
import com.minecolonies.core.entity.ai.citizen.guard.AbstractEntityAIGuard;
import com.minecolonies.core.entity.ai.citizen.guard.EntityAIRanger;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

@Mixin(value = EntityAIRanger.class, remap = false)
public abstract class EntityAIRangerMixin extends AbstractEntityAIGuard<JobRanger, AbstractBuildingGuards>
{
	public EntityAIRangerMixin(@NotNull JobRanger job)
	{
		super(job);
	}

	@Inject(method = "<init>", remap = false, at = @At(value = "TAIL"), cancellable = false)
	private void init(JobRanger job, CallbackInfo ci)
	{
		if (this.toolsNeeded.remove(ToolType.BOW))
		{
			this.toolsNeeded.add(ModToolTypes.RANGER_WEAPON.getToolType());
		}

	}

	@Inject(method = "atBuildingActions", remap = false, at = @At(value = "TAIL"), cancellable = true)
	private void atBuildingActions(CallbackInfo ci)
	{
		var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.ranger;

		if (config.canUseCrossbow.get().booleanValue() && config.canShootFireworkRocket.get().booleanValue())
		{
			if (this.worker.getCitizenColonyHandler().getColony().getResearchManager().getResearchEffects().getEffectStrength(ResearchConstants.ARCHER_USE_ARROWS) > 0)
			{
				var inventory = this.worker.getInventoryCitizen();
				var crossbowSlot = InventoryUtils.getFirstSlotOfItemHandlerContainingTool(inventory, ModToolTypes.CROSSBOW.getToolType(), 0, this.building.getMaxToolLevel());

				if (crossbowSlot == -1)
				{
					return;
				}

				InventoryUtils.transferXOfFirstSlotInProviderWithIntoNextFreeSlotInItemHandler(this.building, item -> item.is(Items.FIREWORK_ROCKET), 64, inventory);

				if (this.building.getSetting(ModBuildingModules.REQUEST_FIREWORK_ROCKET).getValue().booleanValue() && InventoryUtils.getItemCountInItemHandler(inventory, item -> item.is(Items.FIREWORK_ROCKET)) < 16)
				{
					this.checkIfRequestForItemExistOrCreateAsync(new ItemStack(Items.FIREWORK_ROCKET), 64, 16);
				}

			}

		}

	}

}
