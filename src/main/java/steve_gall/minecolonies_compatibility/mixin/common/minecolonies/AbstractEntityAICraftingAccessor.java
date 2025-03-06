package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.minecolonies.core.entity.ai.basic.AbstractEntityAICrafting;

@Mixin(value = AbstractEntityAICrafting.class, remap = false)
public interface AbstractEntityAICraftingAccessor
{
	@Invoker(value = "getRequiredProgressForMakingRawMaterial", remap = false)
	int invokeGetRequiredProgressForMakingRawMaterial();
}
