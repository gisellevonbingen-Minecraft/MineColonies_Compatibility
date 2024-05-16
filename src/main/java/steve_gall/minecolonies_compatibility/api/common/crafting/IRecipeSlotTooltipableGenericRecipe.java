package steve_gall.minecolonies_compatibility.api.common.crafting;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.crafting.IGenericRecipe;

import net.minecraft.network.chat.Component;

public interface IRecipeSlotTooltipableGenericRecipe extends IGenericRecipe
{
	default List<Component> getRecipeSlotToolTip(RecipeSlotRole role, int index)
	{
		return Collections.emptyList();
	}

}
