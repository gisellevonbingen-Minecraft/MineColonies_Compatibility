package steve_gall.minecolonies_compatibility.module.common.scguns.building.module;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.api.common.building.module.AbstractCraftingModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.module.common.scguns.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.scguns.network.GunBenchOpenTeachMessage;
import top.ribs.scguns.block.GunBenchBlock;

public class GunBenchCraftingModule extends AbstractCraftingModuleWithExternalWorkingBlocks
{
	public GunBenchCraftingModule(JobEntry jobEntry)
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
		return state.getBlock() instanceof GunBenchBlock;
	}

	@Override
	public boolean canBlockRecipeWorking(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IRecipeStorage recipeStorage)
	{
		return super.canBlockRecipeWorking(level, pos, state, recipeStorage);
	}

	@Override
	public @NotNull String getId()
	{
		return "scguns_gun_bench";
	}

	@Override
	public Set<CraftingType> getSupportedCraftingTypes()
	{
		return Collections.singleton(ModuleCraftingTypes.GUN_BENCH.get());
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
			PacketDistributor.sendToServer(new GunBenchOpenTeachMessage(this));
		}

	}

}
