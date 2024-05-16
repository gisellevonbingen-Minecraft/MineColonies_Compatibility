package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.core.compatibility.jei.GenericRecipeCategory;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import steve_gall.minecolonies_compatibility.api.common.crafting.IRecipeSlotTooltipableGenericRecipe;
import steve_gall.minecolonies_compatibility.api.common.crafting.RecipeSlotRole;

@Mixin(value = GenericRecipeCategory.class, remap = false)
public abstract class GenericRecipeCategoryMixin
{
	@Unique
	private int minecolonies_compatibility$inputIndex = 0;
	@Unique
	private int minecolonies_compatibility$outputIndex = 0;
	@Unique
	private int minecolonies_compatibility$catalystIndex = 0;
	@Unique
	private IRecipeSlotTooltipableGenericRecipe minecolonies_compatibility$recipe = null;

	@Inject(method = "setNormalRecipe", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void setNormalRecipe_Head(@NotNull IRecipeLayoutBuilder builder, @NotNull IGenericRecipe recipe, @NotNull IFocusGroup focuses, CallbackInfo ci)
	{
		this.minecolonies_compatibility$inputIndex = 0;
		this.minecolonies_compatibility$outputIndex = 0;
		this.minecolonies_compatibility$catalystIndex = 0;
		this.minecolonies_compatibility$recipe = recipe instanceof IRecipeSlotTooltipableGenericRecipe genericRecipe ? genericRecipe : null;
	}

	@Redirect(method = "setNormalRecipe", remap = false, at = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;addSlot"))
	private IRecipeSlotBuilder setNormalRecipe_addSlot(IRecipeLayoutBuilder builder, RecipeIngredientRole recipeIngredientRole, int x, int y)
	{
		var slotBuilder = builder.addSlot(recipeIngredientRole, x, y);

		if (this.minecolonies_compatibility$recipe != null)
		{
			RecipeSlotRole role = null;
			var index = -1;

			if (recipeIngredientRole == RecipeIngredientRole.INPUT)
			{
				role = RecipeSlotRole.INPUT;
				index = this.minecolonies_compatibility$inputIndex;
				this.minecolonies_compatibility$inputIndex++;
			}
			else if (recipeIngredientRole == RecipeIngredientRole.OUTPUT)
			{
				role = RecipeSlotRole.OUTPUT;
				index = this.minecolonies_compatibility$outputIndex;
				this.minecolonies_compatibility$outputIndex++;
			}
			else if (recipeIngredientRole == RecipeIngredientRole.CATALYST)
			{
				role = RecipeSlotRole.CATALYST;
				index = this.minecolonies_compatibility$catalystIndex;
				this.minecolonies_compatibility$catalystIndex++;
			}

			if (role != null && index > -1)
			{
				final var tooltipRole = role;
				final var toolTipIndex = index;
				slotBuilder.addTooltipCallback((recipeSlotView, tooltip) -> tooltip.addAll(1, this.minecolonies_compatibility$recipe.getRecipeSlotToolTip(tooltipRole, toolTipIndex)));
			}

		}

		return slotBuilder;
	}

}
