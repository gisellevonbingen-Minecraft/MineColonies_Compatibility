package steve_gall.minecolonies_compatibility.mixin.common.storagenetwork;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lothrazar.storagenetwork.util.UtilConnections;

import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.module.common.storagenetwork.init.ModuleBlocks;

@Mixin(value = UtilConnections.class, remap = false)
public abstract class UtilConnectionsMixin
{
	@Inject(method = "isCableOverride", remap = false, at = @At(value = "RETURN"), cancellable = true)
	private static void isCableOverride(BlockState facingState, CallbackInfoReturnable<Boolean> cir)
	{
		if (facingState.is(ModuleBlocks.CITIZEN_INVENTORY.get()))
		{
			cir.setReturnValue(true);
		}

	}

}
