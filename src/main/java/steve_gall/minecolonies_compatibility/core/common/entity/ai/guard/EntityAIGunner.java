package steve_gall.minecolonies_compatibility.core.common.entity.ai.guard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.minecolonies.core.entity.pathfinding.navigation.MinecoloniesAdvancedPathNavigate;
import com.minecolonies.core.entity.pathfinding.pathjobs.PathJobWalkRandomEdge;

import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.core.common.job.JobGunner;

public class EntityAIGunner extends CustomizableEntityAIGuard<JobGunner, AbstractBuildingGuards>
{
	public EntityAIGunner(@NotNull JobGunner job)
	{
		super(job);

		this.toolsNeeded.add(ModToolTypes.GUN.getToolType());
		new GunnerCombatAI<>((EntityCitizen) this.worker, this.getStateAI(), this);
	}

	@Override
	@Nullable
	public EquipmentTypeEntry getHandToolType()
	{
		return ModToolTypes.GUN.getToolType();
	}

	@Override
	public void guardMovement()
	{
		var worker = this.worker;
		var buildingGuards = this.buildingGuards;
		var guardPos = buildingGuards.getGuardPos(worker);

		if (worker.getRandom().nextInt(30) < 1)
		{
			this.walkToSafePos(guardPos);
			return;
		}

		if (!worker.getNavigation().isDone())
		{
			return;
		}

		if ((BlockPosUtil.dist(guardPos, worker.blockPosition()) <= 10.0D || walkToSafePos(guardPos)))
		{
			// Moves the ranger randomly to close edges, for better vision to mobs
			((MinecoloniesAdvancedPathNavigate) worker.getNavigation()).setPathJob(new PathJobWalkRandomEdge(this.world, guardPos, 10, worker), null, 1.0, true);
		}

	}

}
