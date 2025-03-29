package steve_gall.minecolonies_compatibility.mixin.common.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.core.common.block.entity.INetworkStorageViewHolder;

@Mixin(value = BlockBehaviour.class, remap = true)
public abstract class BlockBehaviourMixin
{
	@Inject(method = "onRemove", remap = true, at = @At(value = "HEAD"), cancellable = true)
	private void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving, CallbackInfo ci)
	{
		if (state.getBlock() != newState.getBlock())
		{
			if (level.getBlockEntity(pos) instanceof INetworkStorageViewHolder viewHolder)
			{
				viewHolder.getNetworkStorageView().unlink();
			}

		}

	}

}
