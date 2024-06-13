package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.colony.buildings.workerbuildings.IWareHouse;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingDeliveryman;
import com.minecolonies.core.colony.jobs.JobDeliveryman;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIInteract;
import com.minecolonies.core.entity.ai.workers.service.EntityAIWorkDeliveryman;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemHandlerHelper;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;

@Mixin(value = EntityAIWorkDeliveryman.class)
public abstract class EntityAIWorkDeliverymanMixin extends AbstractEntityAIInteract<JobDeliveryman, BuildingDeliveryman>
{
	public EntityAIWorkDeliverymanMixin(@NotNull JobDeliveryman job)
	{
		super(job);
	}

	@Shadow(remap = false)
	abstract @Nullable IWareHouse getAndCheckWareHouse();

	@Inject(method = "gatherIfInTileEntity", remap = false, at = @At("TAIL"), cancellable = true)
	private void gatherIfInTileEntity(BlockEntity entity, ItemStack is, CallbackInfoReturnable<Boolean> cir)
	{
		var warehouse = this.getAndCheckWareHouse();

		if (warehouse == null)
		{
			return;
		}

		var module = warehouse.getModule(ModBuildingModules.NETWORK_STORAGE);

		if (module == null)
		{
			return;
		}

		var view = module.getView(entity.getBlockPos());

		if (!module.canExtract(view))
		{
			return;
		}

		var inventory = this.worker.getInventoryCitizen();
		var extracting = view.extractItem(is, true);

		if (extracting.isEmpty())
		{
			return;
		}

		var remain = ItemHandlerHelper.insertItem(inventory, extracting, true);

		if (remain.isEmpty())
		{
			var extracted = view.extractItem(is, false);
			ItemHandlerHelper.insertItem(inventory, extracted, false);
			cir.setReturnValue(true);
		}

	}

}
