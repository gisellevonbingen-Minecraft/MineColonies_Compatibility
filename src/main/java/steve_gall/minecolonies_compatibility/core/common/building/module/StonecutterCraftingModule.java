package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;

import steve_gall.minecolonies_compatibility.core.common.init.ModCraftingTypes;

public class StonecutterCraftingModule extends AbstractCraftingBuildingModule.Custom
{
	public StonecutterCraftingModule(JobEntry jobEntry)
	{
		super(jobEntry);
	}

	@Override
	public void improveRecipe(IRecipeStorage recipe, int count, ICitizenData citizen)
	{

	}

	@Override
	public @NotNull String getId()
	{
		return ModCraftingTypes.STONECUTTING.getId().getPath();
	}

	@Override
	public Set<CraftingType> getSupportedCraftingTypes()
	{
		return Set.of(ModCraftingTypes.STONECUTTING.get());
	}

	@Override
	public boolean isRecipeCompatible(@NotNull IGenericRecipe recipe)
	{
		return true;
	}

}
