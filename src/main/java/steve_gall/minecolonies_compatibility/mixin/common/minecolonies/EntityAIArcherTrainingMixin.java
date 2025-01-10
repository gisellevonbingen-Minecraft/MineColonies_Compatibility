package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.core.entity.ai.workers.guard.training.EntityAIArcherTraining;

import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

@Mixin(value = EntityAIArcherTraining.class, remap = false)
public abstract class EntityAIArcherTrainingMixin
{
	@Redirect(method = "isSetup", remap = false, at = @At(value = "INVOKE", target = "net/minecraftforge/registries/RegistryObject.get"))
	private Object isSetup_ToolType(RegistryObject<?> self)
	{
		if (self == ModEquipmentTypes.bow)
		{
			return ModToolTypes.RANGER_WEAPON.getToolType();
		}

		return self.get();
	}

}
