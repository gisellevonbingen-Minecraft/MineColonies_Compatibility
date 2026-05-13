package steve_gall.minecolonies_compatibility.module.common.scguns.crafting;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleGenericRecipe;
import steve_gall.minecolonies_compatibility.core.common.crafting.IngredientHelper;
import top.ribs.scguns.client.screen.GunBenchRecipe;
import top.ribs.scguns.init.ModBlocks;

public class GunBenchGenericRecipe extends SimpleGenericRecipe
{
	@NotNull
	private final List<ItemStack> blueprint;

	public GunBenchGenericRecipe(RecipeHolder<GunBenchRecipe> recipe, HolderLookup.Provider provider)
	{
		this(recipe.id(), IngredientHelper.getStacksList(IngredientHelper.filterNotEmpty(recipe.value().getIngredients())), IngredientHelper.getStacks(recipe.value().getBlueprint()), recipe.value().getResultItem(provider));
	}

	public GunBenchGenericRecipe(ResourceLocation recipeId, List<List<ItemStack>> ingredients, List<ItemStack> blueprints, ItemStack output)
	{
		super(recipeId, ingredients, output);

		this.blueprint = new ArrayList<>(blueprints);
	}

	@Override
	public @NotNull List<List<ItemStack>> getInputs()
	{
		var list = new ArrayList<>(super.getInputs());

		if (this.blueprint.size() > 0)
		{
			list.add(this.blueprint);
		}

		return list;
	}

	@Override
	public @NotNull List<ItemStack> getAdditionalOutputs()
	{
		var list = new ArrayList<>(super.getAdditionalOutputs());

		if (this.blueprint.size() > 0)
		{
			list.add(this.blueprint.get(0));
		}

		return list;
	}

	@Override
	public @NotNull Block getIntermediate()
	{
		return ModBlocks.GUN_BENCH.get();
	}

}
