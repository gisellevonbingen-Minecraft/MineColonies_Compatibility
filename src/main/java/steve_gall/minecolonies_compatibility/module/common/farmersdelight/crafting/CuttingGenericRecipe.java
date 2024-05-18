package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.util.OptionalPredicate;
import com.minecolonies.api.util.constant.IToolType;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.crafting.IRecipeSlotTooltipableGenericRecipe;
import steve_gall.minecolonies_compatibility.api.common.crafting.RecipeSlotRole;
import steve_gall.minecolonies_compatibility.core.common.crafting.IngredientHelper;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.FarmersDelightModule;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class CuttingGenericRecipe implements IRecipeSlotTooltipableGenericRecipe
{
	private final ResourceLocation recipeId;
	private final List<List<ItemStack>> input;
	private final List<CuttingChanceResult> allResults;
	private final List<ItemStack> primaryOutputs;
	private final List<CuttingChanceResult> additionalResults;
	private final List<ItemStack> additionalOutputs;
	private final IToolType toolType;

	public CuttingGenericRecipe(CuttingBoardRecipe recipe, IToolType toolType)
	{
		this(recipe.getId(), IngredientHelper.getStacksList(recipe.getIngredients()), recipe.getRollableResults().stream().map(CuttingChanceResult::new).toList(), toolType);
	}

	public CuttingGenericRecipe(ResourceLocation recipeId, List<List<ItemStack>> ingredients, List<CuttingChanceResult> results, IToolType toolType)
	{
		this.recipeId = recipeId;
		this.input = ingredients;
		this.allResults = results;
		this.primaryOutputs = new ArrayList<>();
		this.additionalResults = new ArrayList<>();
		this.additionalOutputs = new ArrayList<>();

		for (var result : results)
		{
			if (result.getChance() >= 1.0F && this.primaryOutputs.size() == 0)
			{
				this.primaryOutputs.add(result.getStack());
			}
			else
			{
				this.additionalResults.add(result);
				this.additionalOutputs.add(result.getStack());
			}

		}

		this.toolType = toolType;
	}

	@Override
	public List<Component> getRecipeSlotToolTip(RecipeSlotRole role, int index)
	{
		if (role == RecipeSlotRole.OUTPUT)
		{
			var result = this.allResults.get(index);
			return FarmersDelightModule.getChanceTooltip(result.getChance());
		}
		else
		{
			return Collections.emptyList();
		}

	}

	@Override
	public int getGridSize()
	{
		return 1;
	}

	@Override
	public @Nullable ResourceLocation getRecipeId()
	{
		return this.recipeId;
	}

	@Override
	public @NotNull ItemStack getPrimaryOutput()
	{
		return this.primaryOutputs.size() == 0 ? ItemStack.EMPTY : this.primaryOutputs.get(0);
	}

	@Override
	public @NotNull List<ItemStack> getAllMultiOutputs()
	{
		return this.primaryOutputs;
	}

	@Override
	public @NotNull List<ItemStack> getAdditionalOutputs()
	{
		return this.additionalOutputs;
	}

	public @NotNull List<CuttingChanceResult> getAdditionalResults()
	{
		return this.additionalResults;
	}

	@Override
	public @NotNull List<List<ItemStack>> getInputs()
	{
		return this.input;
	}

	@Override
	public Optional<Boolean> matchesOutput(@NotNull OptionalPredicate<ItemStack> predicate)
	{
		return Optional.empty();
	}

	@Override
	public Optional<Boolean> matchesInput(@NotNull OptionalPredicate<ItemStack> predicate)
	{
		return Optional.empty();
	}

	@Override
	public @NotNull Block getIntermediate()
	{
		return ModBlocks.CUTTING_BOARD.get();
	}

	@Override
	public @Nullable ResourceLocation getLootTable()
	{
		return null;
	}

	@Override
	public @NotNull IToolType getRequiredTool()
	{
		return this.toolType;
	}

	@Override
	public @Nullable LivingEntity getRequiredEntity()
	{
		return null;
	}

	@Override
	public @NotNull List<Component> getRestrictions()
	{
		return Collections.emptyList();
	}

	@Override
	public int getLevelSort()
	{
		return -1;
	}

}
