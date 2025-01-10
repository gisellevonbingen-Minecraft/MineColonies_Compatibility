package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.core.entity.ai.citizen.trainingcamps.EntityAICombatTraining;

import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

@Mixin(value = EntityAICombatTraining.class, remap = false)
public abstract class EntityAICombatTrainingMixin
{
	@Redirect(method = "isSetup", remap = false, at = @At(value = "FIELD", target = "com/minecolonies/api/util/constant/ToolType.SWORD", opcode = Opcodes.GETSTATIC))
	private ToolType isSetup_ToolType()
	{
		return ModToolTypes.KNIGHT_WEAPON.getToolType();
	}

}
