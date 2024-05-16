package steve_gall.minecolonies_compatibility.core.common.entity.ai.guard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.entity.ai.workers.guard.AbstractEntityAIGuard;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.minecolonies.core.entity.pathfinding.navigation.MinecoloniesAdvancedPathNavigate;
import com.minecolonies.core.entity.pathfinding.pathjobs.PathJobWalkRandomEdge;

import steve_gall.minecolonies_compatibility.api.common.entity.ai.ICustomizableEntityAI;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.core.common.job.JobGunner;

public class EntityAIGunner extends AbstractEntityAIGuard<JobGunner, AbstractBuildingGuards> implements ICustomizableEntityAI
{
	public EntityAIGunner(@NotNull JobGunner job)
	{
		super(job);

		this.toolsNeeded.add(ModToolTypes.GUN.getToolType());
		new GunnerCombatAI<>((EntityCitizen) this.worker, this.getStateAI(), this);
	}

	@Override
	@Nullable
	public ToolType getHandToolType()
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
			worker.isWorkerAtSiteWithMove(buildingGuards.getGuardPos(), 3);
			return;
		}

		if (worker.isWorkerAtSiteWithMove(buildingGuards.getGuardPos(), 10) || Math.abs(buildingGuards.getGuardPos().getY() - worker.blockPosition().getY()) > 3)
		{
			// Moves the ranger randomly to close edges, for better vision to mobs
			((MinecoloniesAdvancedPathNavigate) worker.getNavigation()).setPathJob(new PathJobWalkRandomEdge(this.world, buildingGuards.getGuardPos(), 20, worker), null, 1.0, true);
		}

	}

}
