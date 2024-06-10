package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.core.colony.buildings.workerbuildings.BuildingDeliveryman;
import com.minecolonies.core.colony.jobs.JobDeliveryman;
import com.minecolonies.core.entity.ai.basic.AbstractEntityAIInteract;
import com.minecolonies.core.entity.ai.citizen.deliveryman.EntityAIWorkDeliveryman;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;

@Mixin(value = EntityAIWorkDeliveryman.class)
public abstract class EntityAIWorkDeliverymanMixin extends AbstractEntityAIInteract<JobDeliveryman, BuildingDeliveryman>
{
	public EntityAIWorkDeliverymanMixin(@NotNull JobDeliveryman job)
	{
		super(job);
	}

	@Inject(method = "gatherIfInTileEntity", remap = false, at = @At("TAIL"), cancellable = true)
	private void gatherIfInTileEntity(BlockEntity entity, ItemStack is, CallbackInfoReturnable<Boolean> cir)
	{
		var inventory = this.worker.getInventoryCitizen();
		var view = NetworkStorageModule.resolveView(entity);

		if (view != null)
		{
			if (view.extract(inventory, is))
			{
				cir.setReturnValue(true);
			}

		}

	}

}
