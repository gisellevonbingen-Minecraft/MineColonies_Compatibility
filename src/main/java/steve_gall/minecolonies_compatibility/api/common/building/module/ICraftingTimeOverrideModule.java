package steve_gall.minecolonies_compatibility.api.common.building.module;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.buildings.modules.ICraftingBuildingModule;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.core.BlockPos;

public interface ICraftingTimeOverrideModule extends ICraftingBuildingModule
{
	int getCraftingTime(@NotNull AbstractEntityCitizen worker, @NotNull BlockPos workingPos, @NotNull IRecipeStorage recipeStorage);
}
