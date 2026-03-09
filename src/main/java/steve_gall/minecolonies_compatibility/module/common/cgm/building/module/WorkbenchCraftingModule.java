package steve_gall.minecolonies_compatibility.module.common.cgm.building.module;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.mrcrayfish.guns.block.WorkbenchBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.building.module.AbstractCraftingModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.cgm.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.cgm.network.WorkbenchOpenTeachMessage;

public class WorkbenchCraftingModule extends AbstractCraftingModuleWithExternalWorkingBlocks
{
	public WorkbenchCraftingModule(JobEntry jobEntry)
	{
		super(jobEntry);
	}

	@Override
	public void improveRecipe(IRecipeStorage recipe, int count, ICitizenData citizen)
	{

	}

	@Override
	public boolean isWorkingBlock(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state)
	{
		return level.getBlockState(pos).getBlock() instanceof WorkbenchBlock;
	}

	@Override
	public boolean canBlockRecipeWorking(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IRecipeStorage recipeStorage)
	{
		return super.canBlockRecipeWorking(level, pos, state, recipeStorage);
	}

	@Override
	public @NotNull String getId()
	{
		return "cgm_workbench";
	}

	@Override
	public Set<CraftingType> getSupportedCraftingTypes()
	{
		return Collections.singleton(ModuleCraftingTypes.WORKBENCH.get());
	}

	@Override
	public boolean isRecipeCompatible(@NotNull IGenericRecipe recipe)
	{
		return true;
	}

	public static class View extends CraftingModuleView
	{
		public View()
		{

		}

		@Override
		public void openCraftingGUI()
		{
			MineColoniesCompatibility.network().sendToServer(new WorkbenchOpenTeachMessage(this));
		}

	}

}
