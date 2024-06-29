package steve_gall.minecolonies_compatibility.mixin.client.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.controls.ItemIcon;
import com.minecolonies.api.util.constant.WindowConstants;
import com.minecolonies.core.client.gui.modules.WindowListRecipes;

import steve_gall.minecolonies_compatibility.core.client.gui.ItemIconExtension;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CuttingRecipeStorage;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizableRecipeStorage;

@Mixin(targets = "com.minecolonies.core.client.gui.modules.WindowListRecipes$1", remap = false)
public abstract class WindowListRecipes1Mixin
{
	@Unique
	private WindowListRecipes minecolonies_compatibility$this$0;

	private WindowListRecipes getOuter() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		if (minecolonies_compatibility$this$0 == null)
		{
			var field = this.getClass().getDeclaredField("this$0");
			field.setAccessible(true);
			minecolonies_compatibility$this$0 = (WindowListRecipes) field.get(this);
		}

		return minecolonies_compatibility$this$0;
	}

	@Inject(method = "updateElement", remap = false, at = @At(value = "TAIL"), cancellable = true)
	private void updateElement(int index, Pane rowPane, CallbackInfo ci) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		if (ModuleManager.FARMERSDELIGHT.isLoaded())
		{
			var accessor = (WindowListRecipesAcccessor) this.getOuter();

			if (accessor.getModule().getRecipes().get(index) instanceof ICustomizableRecipeStorage recipe && recipe.getImpl() instanceof CuttingRecipeStorage cutting)
			{
				var displayStacks = recipe.getRecipeType().getOutputDisplayStacks();
				var outputIndex = (accessor.getLifeCount() / WindowConstants.LIFE_COUNT_DIVIDER) % displayStacks.size();
				var results = cutting.getResults();

				if (0 <= outputIndex && outputIndex < results.size())
				{
					var result = results.get(outputIndex);
					var icon = rowPane.findPaneOfTypeByID(WindowListRecipesAcccessor.getOutputIcon(), ItemIcon.class);
					((ItemIconExtension) icon).minecolonies_compatibility$setFarmersChance(result.getChance());
				}

			}

		}

	}

}
