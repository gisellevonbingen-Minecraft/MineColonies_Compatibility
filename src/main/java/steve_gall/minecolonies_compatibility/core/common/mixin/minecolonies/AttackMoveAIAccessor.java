package steve_gall.minecolonies_compatibility.core.common.mixin.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.minecolonies.core.entity.ai.combat.AttackMoveAI;

@Mixin(value = AttackMoveAI.class, remap = false)
public interface AttackMoveAIAccessor
{
	@Accessor(value = "nextAttackTime")
	long getNextAttackTime();
}
