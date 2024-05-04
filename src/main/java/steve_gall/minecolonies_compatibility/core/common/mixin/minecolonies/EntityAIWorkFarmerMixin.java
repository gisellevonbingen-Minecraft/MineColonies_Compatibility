package steve_gall.minecolonies_compatibility.core.common.mixin.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingFarmer;
import com.minecolonies.core.colony.jobs.JobFarmer;
import com.minecolonies.core.entity.ai.workers.crafting.AbstractEntityAICrafting;
import com.minecolonies.core.entity.ai.workers.production.agriculture.EntityAIWorkFarmer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.PlantSeedContext;

@Mixin(value = EntityAIWorkFarmer.class, remap = false)
public abstract class EntityAIWorkFarmerMixin extends AbstractEntityAICrafting<JobFarmer, BuildingFarmer>
{
	@Nullable
	@Shadow
	private BlockPos prevPos;

	public EntityAIWorkFarmerMixin(@NotNull JobFarmer job)
	{
		super(job);
	}

	@Inject(method = "plantCrop", at = @At(value = "HEAD"), cancellable = true)
	private void plantCrop(ItemStack stack, @NotNull BlockPos position, CallbackInfoReturnable<Boolean> cir)
	{
		if (stack == null || stack.isEmpty())
		{
			cir.setReturnValue(false);
			return;
		}

		var worker = this.worker;
		var slot = worker.getCitizenInventoryHandler().findFirstSlotInInventoryWith(stack.getItem());

		if (slot == -1)
		{
			cir.setReturnValue(false);
			return;
		}

		var level = this.world;
		var plantPosition = position.above();

		if (!level.getBlockState(plantPosition).isAir())
		{
			cir.setReturnValue(true);
			return;
		}

		var context = new PlantSeedContext(level, plantPosition, stack);
		var crop = CustomizedCrop.selectBySeed(context);
		BlockState plantState = null;

		if (crop != null)
		{
			plantState = crop.getPlantState(context);

			if (plantState == null)
			{
				cir.setReturnValue(true);
				return;
			}

		}
		else if (stack.getItem() instanceof BlockItem item && item.getBlock() instanceof StemBlock block)
		{
			if (this.prevPos != null && !level.isEmptyBlock(this.prevPos.above()))
			{
				cir.setReturnValue(true);
				return;
			}

			plantState = block.getPlant(level, plantPosition);
		}

		if (plantState != null)
		{
			level.setBlock(plantPosition, plantState, Block.UPDATE_ALL);
			worker.decreaseSaturationForContinuousAction();
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

		var level = this.world;
		var plantPosition = position.above();
		var state = level.getBlockState(plantPosition);
		var context = new PlantBlockContext(level, plantPosition, state);
		var crop = CustomizedCrop.selectByCrop(context);

		if (crop != null)
		{
			var specialPositionFunction = crop.getSpecialHarvestPosition(context);

			if (specialPositionFunction != null)
			{
				var harvestPosition = specialPositionFunction.apply(context);

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
		else if (state.getBlock() instanceof StemGrownBlock)
		{
			cir.setReturnValue(position);
		}

	}

	@Redirect(method = "harvestIfAble", at = @At(value = "INVOKE", target = "mineBlock(Lnet/minecraft/core/BlockPos;)Z"))
	private boolean harvestIfAble_mineBlock(EntityAIWorkFarmer self, BlockPos position)
	{
		var worker = this.worker;
		var level = this.world;
		var state = level.getBlockState(position);
		var context = new PlantBlockContext(level, position, state);
		var crop = CustomizedCrop.selectByCrop(context);

		if (crop != null)
		{
			var method = crop.getSpecialHarvestMethod(context);

			if (method != null)
			{
				if (!this.holdEfficientTool(state, position))
				{
					return false;
				}
				else if (this.hasNotDelayed(this.getBlockMiningDelay(state, position)))
				{
					return false;
				}

				var drops = this.increaseBlockDrops(method.harvest(context));
				var inventory = worker.getInventoryCitizen();
				var hand = worker.getUsedItemHand();

				for (var stack : drops)
				{
					InventoryUtils.transferItemStackIntoNextBestSlotInItemHandler(stack, inventory);
				}

				worker.swing(hand);
				worker.getCitizenItemHandler().damageItemInHand(hand, 1);

				this.incrementActionsDone();
				worker.decreaseSaturationForContinuousAction();

				this.onBlockDropReception(drops);

				return true;
			}

		}

		return this.mineBlock(position);
	}

	@ModifyVariable(method = "getSurfacePos(Lnet/minecraft/core/BlockPos;Ljava/lang/Integer;)Lnet/minecraft/core/BlockPos;", at = @At("STORE"), ordinal = 0)
	private Block getSurfacePos(Block curBlock)
	{
		if (curBlock instanceof StemGrownBlock)
		{
			return Blocks.PUMPKIN;
		}
		else
		{
			return curBlock;
		}

	}

}
