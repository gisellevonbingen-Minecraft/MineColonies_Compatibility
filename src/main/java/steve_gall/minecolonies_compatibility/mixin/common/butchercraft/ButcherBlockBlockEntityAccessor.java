package steve_gall.minecolonies_compatibility.mixin.common.butchercraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockBlockEntity;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockRecipe;

@Mixin(value = ButcherBlockBlockEntity.class, remap = false)
public interface ButcherBlockBlockEntityAccessor
{
	@Invoker(value = "setupStage", remap = false)
	void invokeSetupStage(ButcherBlockRecipe r, int i);
}
