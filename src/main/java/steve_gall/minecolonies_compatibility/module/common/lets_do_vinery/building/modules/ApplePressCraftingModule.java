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
import satisfyu.vinery.block.WinePressBlock;
import satisfyu.vinery.registry.ObjectRegistry;
import steve_gall.minecolonies_compatibility.api.common.building.module.AbstractCraftingModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.core.common.util.InteractionMessageHelper;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init.ModuleCraftingTypes;

public class ApplePressCraftingModule extends AbstractCraftingModuleWithExternalWorkingBlocks
{
	public ApplePressCraftingModule(JobEntry jobEntry)
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
		return state.getBlock() instanceof WinePressBlock;
	}

	@Override
	public @NotNull String getId()
	{
		return "lets_do_vinery_apple_press";
	}

	@Override
	public Set<CraftingType> getSupportedCraftingTypes()
	{
		return Collections.singleton(ModuleCraftingTypes.APPLE_PRESS.get());
	}

	@Override
	public boolean isRecipeCompatible(@NotNull IGenericRecipe recipe)
	{
		return true;
	}

	@Override
	public @NotNull Component getWorkingBlockNotFoundMessage(@NotNull IRecipeStorage recipeStorage)
	{
		return InteractionMessageHelper.getWorkingBlockNotFound(ObjectRegistry.WINE_PRESS.get());
	}

}
