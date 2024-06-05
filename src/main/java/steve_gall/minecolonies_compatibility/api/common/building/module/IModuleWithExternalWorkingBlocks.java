package steve_gall.minecolonies_compatibility.api.common.building.module;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.buildings.modules.IModuleWithExternalBlocks;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.research.util.ResearchConstants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.PathJobFindWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.WorkingBlocksPathResult;

public interface IModuleWithExternalWorkingBlocks extends IModuleWithExternalBlocks
{
	default @NotNull PathJobFindWorkingBlocks<?> createWorkingBlocksFindPath(@Nullable AbstractEntityCitizen citizen)
	{
		return this.createWorkingBlocksFindPath(citizen, this.createPathResult(citizen));
	}

	default @NotNull PathJobFindWorkingBlocks<?> createWorkingBlocksFindPath(@Nullable AbstractEntityCitizen citizen, WorkingBlocksPathResult result)
	{
		var building = this.getBuilding();
		var corners = building.getCorners();
		var colony = building.getColony();
		var job = new PathJobFindWorkingBlocks<>(colony.getWorld(), citizen != null ? citizen.blockPosition() : building.getID(), BoundingBox.fromCorners(corners.getA(), corners.getB()), citizen, result);

		if (citizen != null)
		{
			job.setPathingOptions(citizen.getNavigation().getPathingOptions());
		}
		else
		{
			var researchEffects = colony.getResearchManager().getResearchEffects();
			var options = job.getPathingOptions();
			options.setEnterDoors(true);
			options.setCanOpenDoors(true);
			options.setCanUseRails(researchEffects.getEffectStrength(ResearchConstants.RAILS) > 0);
			options.setCanClimbAdvanced(researchEffects.getEffectStrength(ResearchConstants.VINES) > 0);
		}

		return job;
	}

	@NotNull
	WorkingBlocksPathResult createPathResult(@Nullable AbstractEntityCitizen citizen);

	/**
	 *
	 * @return true if finding path
	 */
	boolean requestFindWorkingBlocks();

	/**
	 *
	 * @param citizen
	 * @return true if finding path
	 */
	boolean requestFindWorkingBlocks(@Nullable AbstractEntityCitizen citizen);

	boolean addWorkingBlock(@Nullable BlockPos pos);

	boolean removeWorkingBlock(@Nullable BlockPos pos);

	@NotNull
	default Stream<BlockPos> getWorkingBlocks()
	{
		var level = this.getBuilding().getColony().getWorld();

		return this.getRegisteredBlocks().stream().filter(pos ->
		{
			var state = level.getBlockState(pos);

			if (this.isWorkingBlock(level, pos, state))
			{
				return true;
			}

			this.removeWorkingBlock(pos);
			return false;
		});
	}

	boolean isWorkingBlock(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state);

	@NotNull
	default BlockPos getWalkingPosition(@NotNull BlockPos pos)
	{
		return pos;
	}

}
