package steve_gall.minecolonies_compatibility.api.common.crafting;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;

import net.minecraft.network.chat.Component;

public interface ICustomGenericRecipe extends IGenericRecipe
{
	boolean test(AbstractCraftingBuildingModule module);

	default List<Component> getOutputToolTip(int outputIndex)
	{
		return Collections.emptyList();
	}

}
