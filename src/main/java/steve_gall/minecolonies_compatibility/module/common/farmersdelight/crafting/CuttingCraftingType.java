package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.RecipeCraftingType;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class CuttingCraftingType extends RecipeCraftingType<RecipeWrapper, CuttingBoardRecipe>
{
	public CuttingCraftingType(@NotNull ResourceLocation id)
	{
		super(id, null, null);
	}

	@Override
	public @NotNull List<IGenericRecipe> findRecipes(@NotNull RecipeManager recipeManager, @Nullable Level world)
	{
		var recipes = new ArrayList<IGenericRecipe>();

		for (var recipe : recipeManager.getAllRecipesFor(ModRecipeTypes.CUTTING.get()))
		{
			var toolTypes = Arrays.stream(recipe.getTool().getItems()).map(this::getToolType).distinct().collect(Collectors.toList());
			toolTypes.remove(ToolType.NONE);

			recipes.add(new CuttingGenericRecipe(recipe, toolTypes.size() > 0 ? toolTypes.get(0) : ToolType.NONE));
		}

		return recipes;
	}

	private ToolType getToolType(ItemStack stack)
	{
		for (var toolType : ToolType.values())
		{
			if (ItemStackUtils.isTool(stack, toolType))
			{
				return toolType;
			}

		}

		return ToolType.NONE;
	}

}
