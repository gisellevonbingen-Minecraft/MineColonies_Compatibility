package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.building.modules;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.satisfy.vinery.core.block.ApplePressBlock;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import steve_gall.minecolonies_compatibility.api.common.building.module.AbstractCraftingModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.core.common.util.InteractionMessageHelper;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init.ModuleCraftingTypes;

public class ApplePressMashingCraftingModule extends AbstractCraftingModuleWithExternalWorkingBlocks
{
	public ApplePressMashingCraftingModule(JobEntry jobEntry)
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
		return state.getBlock() instanceof ApplePressBlock && state.getValue(ApplePressBlock.HALF) == DoubleBlockHalf.LOWER;
	}

	@Override
	public boolean needWorkingBlock(@NotNull IRecipeStorage recipeStorage)
	{
		return true;
	}

	@Override
	public @NotNull String getId()
	{
		return "lets_do_vinery_apple_press";
	}

	@Override
	public Set<CraftingType> getSupportedCraftingTypes()
	{
		return Collections.singleton(ModuleCraftingTypes.APPLE_PRESS_MASHING.get());
	}

	@Override
	public boolean isRecipeCompatible(@NotNull IGenericRecipe recipe)
	{
		return true;
	}

	@Override
	public @NotNull Component getWorkingBlockNotFoundMessage(@NotNull IRecipeStorage recipeStorage)
	{
		return InteractionMessageHelper.getWorkingBlockNotFound(ObjectRegistry.APPLE_PRESS.get());
	}

}
