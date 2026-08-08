package steve_gall.minecolonies_compatibility.module.common.lets_do_bakery.building.module;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import satisfy.bakery.block.StoveBlock;
import satisfy.bakery.entity.StoveBlockEntity;
import steve_gall.minecolonies_compatibility.api.common.building.module.AbstractCraftingModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.building.module.ICraftingTimeOverrideModule;
import steve_gall.minecolonies_compatibility.module.common.lets_do_bakery.init.ModuleCraftingTypes;

public class StoveCraftingModule extends AbstractCraftingModuleWithExternalWorkingBlocks implements ICraftingTimeOverrideModule
{
	public StoveCraftingModule(JobEntry jobEntry)
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
		return state.getBlock() instanceof StoveBlock;
	}

	@Override
	public @NotNull String getId()
	{
		return "lets_do_bakery_stove";
	}

	@Override
	public Set<CraftingType> getSupportedCraftingTypes()
	{
		return Collections.singleton(ModuleCraftingTypes.STOVE.get());
	}

	@Override
	public boolean isRecipeCompatible(@NotNull IGenericRecipe recipe)
	{
		return true;
	}

	@Override
	public @NotNull Component getWorkingBlockNotFoundMessage(@NotNull IRecipeStorage recipeStorage)
	{
		return Component.translatable("minecolonies_compatibility.interaction.no_lets_do_bakery_stove");
	}

	@Override
	public int getCraftingTime(@NotNull AbstractEntityCitizen worker, @NotNull BlockPos workingPos, @NotNull IRecipeStorage recipeStorage)
	{
		return StoveBlockEntity.TOTAL_COOKING_TIME;
	}

}
