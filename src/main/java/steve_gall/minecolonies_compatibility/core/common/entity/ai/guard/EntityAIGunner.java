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

		if (worker.getRandom().nextInt(3) < 1)
		{
			this.walkToSafePos(buildingGuards.getGuardPos(worker));
			return;
		}

		if ((BlockPosUtil.dist(buildingGuards.getGuardPos(worker), worker.blockPosition()) <= 10.0D || walkToSafePos(buildingGuards.getGuardPos(worker))) || Math.abs(buildingGuards.getGuardPos(worker).getY() - worker.blockPosition().getY()) > 3)
		{
			// Moves the ranger randomly to close edges, for better vision to mobs
			((MinecoloniesAdvancedPathNavigate) worker.getNavigation()).setPathJob(new PathJobWalkRandomEdge(this.world, buildingGuards.getGuardPos(worker), 20, worker), null, 1.0, true);
		}

	}

}
