package steve_gall.minecolonies_compatibility.core.common.mixin.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.core.colony.buildings.workerbuildings.BuildingFarmer;
import com.minecolonies.core.colony.jobs.JobFarmer;
import com.minecolonies.core.entity.ai.workers.crafting.AbstractEntityAICrafting;
import com.minecolonies.core.entity.ai.workers.production.agriculture.EntityAIWorkFarmer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedCrop;

@Mixin(value = EntityAIWorkFarmer.class, remap = false)
public abstract class EntityAIWorkFarmerMixin extends AbstractEntityAICrafting<JobFarmer, BuildingFarmer>
{
	public EntityAIWorkFarmerMixin(@NotNull JobFarmer job)
	{
		super(job);
	}

	@Inject(method = "plantCrop", at = @At(value = "TAIL"), cancellable = true)
	private void plantCrop(ItemStack stack, @NotNull BlockPos position, CallbackInfoReturnable<Boolean> cir)
	{
		var plantPosition = position.above();
		var crop = CustomizedCrop.selectBySeed(stack);

		if (crop == null || !this.world.isEmptyBlock(plantPosition))
		{
			return;
		}

		var state = crop.getPlantState(stack, this.world, plantPosition);

		if (state != null)
		{
			var slot = this.worker.getCitizenInventoryHandler().findFirstSlotInInventoryWith(stack.getItem());
			this.world.setBlock(plantPosition, state, Block.UPDATE_ALL);
			this.worker.decreaseSaturationForContinuousAction();
			this.getInventory().extractItem(slot, 1, false);
			cir.setReturnValue(true);
		}

	}

	@Inject(method = "findHarvestableSurface", at = @At(value = "RETURN"), cancellable = true)
	private void findHarvestableSurface(@NotNull BlockPos position, CallbackInfoReturnable<BlockPos> cir)
	{
		if (position == null)
		{
			return;
		}

		var plantPosition = position.above();
		var state = this.world.getBlockState(plantPosition);
		var crop = CustomizedCrop.selectByCrop(state);

		if (crop != null && crop.hasSpecialHarvestPosition(state, this.world, plantPosition))
		{
			var harvestPosition = crop.getSpecialHarvestPosition(state, this.world, plantPosition);

			if (harvestPosition != null)
			{
				cir.setReturnValue(harvestPosition.below());
			}
			else
			{
				cir.setReturnValue(null);
			}

		}

	}

}
