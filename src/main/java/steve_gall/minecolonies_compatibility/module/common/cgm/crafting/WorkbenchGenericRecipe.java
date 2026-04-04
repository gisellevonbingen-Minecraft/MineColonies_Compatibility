package steve_gall.minecolonies_compatibility.module.common.cgm.crafting;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.init.ModBlocks;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleGenericRecipe;
import steve_gall.minecolonies_compatibility.core.common.crafting.IngredientHelper;

public class WorkbenchGenericRecipe extends SimpleGenericRecipe
{
	public WorkbenchGenericRecipe(WorkbenchRecipe recipe, RegistryAccess registryAccess)
	{
		this(recipe.getId(), recipe.getMaterials().stream().map(material ->
		{
			List<ItemStack> stacks = new ArrayList<>();

			for (var stack : IngredientHelper.getStacks(material))
			{
				stacks.add(stack.copyWithCount(material.getCount()));
			}

			return stacks;
		}).toList(), recipe.getResultItem(registryAccess));
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
