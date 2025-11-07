package steve_gall.minecolonies_compatibility.mixin.client.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.minecolonies.core.client.gui.modules.WindowListRecipes;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

@Mixin(value = WindowListRecipes.class, remap = false)
public interface WindowListRecipesAcccessor extends AbstractModuleWindowAccessor<CraftingModuleView>
{
	@Accessor(value = "OUTPUT_ICON", remap = false)
	static String getOutputIcon()
	{
		throw new AssertionError();
	}

	@Accessor(value = "lifeCount", remap = false)
	int getLifeCount();
}
