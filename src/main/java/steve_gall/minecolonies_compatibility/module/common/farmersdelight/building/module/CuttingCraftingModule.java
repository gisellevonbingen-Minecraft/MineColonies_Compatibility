package steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableSet;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.constant.IToolType;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.building.module.AbstractCraftingModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.PathJobFindWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.WorkingBlocksPathResult;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleCraftingTypes;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class CuttingCraftingModule extends AbstractCraftingModuleWithExternalWorkingBlocks
{
	private final IToolType toolType;

	public CuttingCraftingModule(JobEntry jobEntry, IToolType toolType)
	{
		super(jobEntry);

		this.toolType = toolType;
	}

	@Override
	public void serializeToView(@NotNull FriendlyByteBuf buf)
	{
		super.serializeToView(buf);

		buf.writeUtf(this.getToolType().getName());
	}

	@Override
	public boolean isIntermediate(@NotNull Block intermediateBlock)
	{
		return intermediateBlock == ModBlocks.CUTTING_BOARD.get();
	}

	@Override
	public @NotNull BlockPos getWalkingPosition(@NotNull BlockPos pos)
	{
		return pos.below();
	}

	@Override
	public @NotNull BlockPos getParticlePosition(@NotNull BlockPos pos)
	{
		return pos.below().below();
	}

	@Override
	public @NotNull String getId()
	{
		return "farmers_cutting_" + this.getToolType().getName();
	}

	@Override
	public Set<CraftingType> getSupportedCraftingTypes()
	{
		return (this.building == null || this.building.getBuildingLevel() >= 3) ? Collections.singleton(ModuleCraftingTypes.CUTTING.get()) : ImmutableSet.of();
	}

	@Override
	public boolean isRecipeCompatible(@NotNull IGenericRecipe recipe)
	{
		return recipe.getRequiredTool() == this.getToolType();
	}

	@Override
	@NotNull
	public Component getWorkingBlockNotFoundMessage()
	{
		return Component.translatable("minecolonies_compatibility.interaction.no_cutting_board");
	}

	public IToolType getToolType()
	{
		return this.toolType;
	}

	@Override
	public WorkingBlocksPathResult createPathResult(@Nullable AbstractEntityCitizen citizen)
	{
		return new WorkingBlocksPathResult(this)
		{
			@Override
			public boolean test(@NotNull PathJobFindWorkingBlocks<?> job, @NotNull BlockPos.MutableBlockPos pos)
			{
				return super.test(job, pos) || super.test(job, pos.setY(pos.getY() + 1));
			}

		};
	}

}
