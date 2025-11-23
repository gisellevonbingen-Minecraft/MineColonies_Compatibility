package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.core.entity.ai.workers.guard.training.EntityAIArcherTraining;

import net.neoforged.neoforge.registries.DeferredHolder;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

@Mixin(value = EntityAIArcherTraining.class, remap = false)
public abstract class EntityAIArcherTrainingMixin
{
	@WrapOperation(method = "isSetup", remap = false, at = @At(value = "INVOKE", target = "net/neoforged/neoforge/registries/DeferredHolder.get"))
	private Object isSetup_ToolType(DeferredHolder<?, ?> self, Operation<Object> operation)
	{
		if (self == ModEquipmentTypes.bow)
		{
			return ModToolTypes.RANGER_WEAPON.getToolType();
		}

		return operation.call(self);
	}

}
