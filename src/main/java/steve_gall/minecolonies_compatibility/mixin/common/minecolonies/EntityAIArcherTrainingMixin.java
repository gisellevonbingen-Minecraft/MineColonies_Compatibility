package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.core.entity.ai.citizen.trainingcamps.EntityAIArcherTraining;

import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

@Mixin(value = EntityAIArcherTraining.class, remap = false)
public abstract class EntityAIArcherTrainingMixin
{
	@Redirect(method = "isSetup", remap = false, at = @At(value = "FIELD", target = "com/minecolonies/api/util/constant/ToolType.BOW", opcode = Opcodes.GETSTATIC))
	private ToolType isSetup_ToolType()
	{
		return ModToolTypes.RANGER_WEAPON.getToolType();
	}

}
