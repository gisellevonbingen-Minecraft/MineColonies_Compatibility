package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.crafting.GenericRecipe;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.api.util.constant.TranslationConstants;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import steve_gall.minecolonies_compatibility.core.common.init.ModTags;

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
		var registryAccess = level.registryAccess();

		for (var recipe : level.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING))
		{
			var accesor = (SmithingRecipeAccessor) recipe;
			var template = IngredientHelper.getStacks(accesor.getTemplate());
			var base = IngredientHelper.getStacks(accesor.getBase());
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
				list.addAll(of(recipe, registryAccess, template, base, levelAddition, i));
			}

			if (remainedAddition.size() > 0)
			{
				list.addAll(of(recipe, registryAccess, template, base, new ArrayList<>(remainedAddition), -1));
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

	public static List<IGenericRecipe> of(SmithingRecipe recipe, RegistryAccess registryAccess, List<ItemStack> template, List<ItemStack> base, List<ItemStack> addition, int requiredLevel)
	{
		return template.stream().map(t -> of(recipe, registryAccess, t, base, addition, requiredLevel)).toList();
	}

	public static IGenericRecipe of(SmithingRecipe recipe, RegistryAccess registryAccess, ItemStack template, List<ItemStack> base, List<ItemStack> addition, int requiredLevel)
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
		input.add(Arrays.asList(template.copyWithCount(template.getCount() * 2)));
		input.add(base);
		input.add(addition);
		var allResults = getAllResults(recipe, registryAccess, template, base, addition);
		return new GenericRecipe(recipe.getId(), ItemStack.EMPTY, allResults, Arrays.asList(template), input, 2, Blocks.AIR, null, ModEquipmentTypes.none.get(), null, restrictions, requiredLevel);
	}

	private static List<ItemStack> getAllResults(SmithingRecipe recipe, RegistryAccess registryAccess, ItemStack template, List<ItemStack> bases, List<ItemStack> additions)
	{
		var list = new ArrayList<ItemStack>();

		for (var base : bases)
		{
			for (var addition : additions)
			{
				var container = new SimpleContainer(3);
				container.setItem(0, template);
				container.setItem(1, base);
				container.setItem(2, addition);
				list.add(recipe.assemble(container, registryAccess));
			}

		}

		return list;
	}

}
