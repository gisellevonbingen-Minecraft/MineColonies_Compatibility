package steve_gall.minecolonies_compatibility.api.common.entity;

import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.combat.threat.IThreatTableEntity;
import com.minecolonies.api.entity.pathfinding.PathResult;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public interface ICustomizableAttackMoveAI<ENTITY_AI extends ICustomizableEntityAI, USER extends Mob & IThreatTableEntity> extends ICustomizableStateAI<ENTITY_AI>
{
	@Nullable
	PathResult<?> createPathResult(LivingEntity target, USER user, double speed);
}
