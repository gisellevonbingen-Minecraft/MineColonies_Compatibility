package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.core.entity.ai.workers.guard.training.EntityAICombatTraining;

import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

@Mixin(value = EntityAICombatTraining.class, remap = false)
public abstract class EntityAICombatTrainingMixin
{
	@Redirect(method = "isSetup", remap = false, at = @At(value = "INVOKE", target = "net/minecraftforge/registries/RegistryObject.get"))
	private Object isSetup_ToolType(RegistryObject<?> self)
	{
		if (self == ModEquipmentTypes.sword)
		{
			return ModToolTypes.KNIGHT_WEAPON.getToolType();
		}

		return self.get();
	}

}
