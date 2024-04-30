package steve_gall.minecolonies_compatibility.api.common.entity.ai;

import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.ai.combat.threat.IThreatTableEntity;
import com.minecolonies.core.entity.pathfinding.pathresults.PathResult;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public interface ICustomizableAttackMoveAI<ENTITY_AI extends ICustomizableEntityAI, USER extends Mob & IThreatTableEntity> extends ICustomizableStateAI<ENTITY_AI>
{
	@SuppressWarnings("rawtypes")
	@Nullable
	PathResult createPathResult(LivingEntity target, USER user, double speed);
}
