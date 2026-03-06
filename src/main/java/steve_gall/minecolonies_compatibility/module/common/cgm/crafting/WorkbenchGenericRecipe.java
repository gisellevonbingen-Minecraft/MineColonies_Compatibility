package steve_gall.minecolonies_compatibility.module.common.cgm.crafting;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.init.ModBlocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleGenericRecipe;
import steve_gall.minecolonies_compatibility.core.common.crafting.IngredientHelper;

public class WorkbenchGenericRecipe extends SimpleGenericRecipe
{
	public WorkbenchGenericRecipe(WorkbenchRecipe recipe)
	{
		this(recipe.getId(), recipe.getMaterials().stream().map(material ->
		{
			var stacks = IngredientHelper.getStacks(material);

			for (var i = 0; i < stacks.size(); i++)
			{
				var stack = stacks.get(i).copy();
				stack.setCount(material.getCount());
				stacks.set(i, stack);
			}

			return stacks;
		}).toList(), recipe.getResultItem());
	}

	public WorkbenchGenericRecipe(ResourceLocation recipeId, List<List<ItemStack>> ingredients, ItemStack output)
	{
		super(recipeId, ingredients, output);
	}

	@Override
	public @NotNull Block getIntermediate()
	{
		return ModBlocks.WORKBENCH.get();
	}

}
