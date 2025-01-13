package steve_gall.minecolonies_compatibility.mixin.common.butchercraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.lance5057.butchercraft.workstations.hook.HookRecipe;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;

@Mixin(value = MeatHookBlockEntity.class, remap = false)
public interface MeatHookBlockEntityAccessor
{
	@Invoker(value = "setupStage", remap = false)
	void invokeSetupStage(HookRecipe r, int i);
}
