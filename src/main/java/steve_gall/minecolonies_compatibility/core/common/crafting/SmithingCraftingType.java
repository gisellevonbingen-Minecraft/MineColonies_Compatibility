package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.crafting.GenericRecipe;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.api.util.constant.TranslationConstants;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import steve_gall.minecolonies_compatibility.core.common.init.ModTags;
import steve_gall.minecolonies_compatibility.mixin.common.minecraft.SmithingRecipeAccessor;

public class SmithingCraftingType extends CraftingType
{
	public SmithingCraftingType(@NotNull ResourceLocation id)
	{
		super(id);
	}

	@Override
	public @NotNull List<IGenericRecipe> findRecipes(@NotNull RecipeManager recipeManager, @Nullable Level level)
	{
		var list = new ArrayList<IGenericRecipe>();
		var tags = ModTags.Items.SMITHING_REQUIRED_LEVEL;

		for (var recipe : level.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING))
		{
			if (recipe instanceof SmithingRecipeAccessor accesor)
			{
				var baseList = IngredientHelper.getStacks(accesor.getBase());

				if (baseList.size() == 0)
				{
					continue;
				}

				var base = Arrays.asList(baseList.get(0));
				var remainedAddition = new HashSet<>(IngredientHelper.getStacks(accesor.getAddition()));

				for (var i = 0; i < tags.length; i++)
				{
					var tag = tags[i];
					var levelAddition = remainedAddition.stream().filter(stack -> stack.is(tag)).toList();

					if (levelAddition.size() == 0)
					{
						continue;
					}

					remainedAddition.removeAll(levelAddition);
					list.add(of(recipe, base, levelAddition, i));
				}

				if (remainedAddition.size() > 0)
				{
					list.add(of(recipe, base, new ArrayList<>(remainedAddition), -1));
				}

			}

		}

		return list;
	}

	public static int getRequiredMinLevel(Ingredient addition)
	{
		var stacks = IngredientHelper.getStacks(addition);
		return stacks.stream().mapToInt(SmithingCraftingType::getRequiredLevel).min().orElse(-1);
	}

	public static int getRequiredLevel(ItemStack addition)
	{
		var tags = ModTags.Items.SMITHING_REQUIRED_LEVEL;

		for (var i = 0; i < tags.length; i++)
		{
			if (addition.is(tags[i]))
			{
				return i;
			}

		}

		return -1;
	}

	public static IGenericRecipe of(UpgradeRecipe recipe, List<ItemStack> base, List<ItemStack> addition, int requiredLevel)
	{
		var maxLevel = Constants.MAX_BUILDING_LEVEL;
		var restrictions = new ArrayList<Component>();

		if (requiredLevel == maxLevel)
		{
			restrictions.add(Component.translatable(TranslationConstants.PARTIAL_JEI_INFO + "onelevelrestriction", requiredLevel));
		}
		else if (requiredLevel > 1)
		{
			restrictions.add(Component.translatable(TranslationConstants.PARTIAL_JEI_INFO + "levelrestriction", requiredLevel, maxLevel));
		}

		var input = new ArrayList<List<ItemStack>>();
		input.add(base);
		input.add(addition);
		var allResults = getAllResults(recipe, base, addition);
		return new GenericRecipe(recipe.getId(), ItemStack.EMPTY, allResults, Collections.emptyList(), input, 2, Blocks.AIR, null, ToolType.NONE, null, restrictions, requiredLevel);
	}

	private static List<ItemStack> getAllResults(UpgradeRecipe recipe, List<ItemStack> bases, List<ItemStack> additions)
	{
		var list = new ArrayList<ItemStack>();

		for (var base : bases)
		{
			for (var addition : additions)
			{
				var container = new SimpleContainer(2);
				container.setItem(0, base);
				container.setItem(1, addition);
				list.add(recipe.assemble(container));
			}

		}

		return list;
	}

}
