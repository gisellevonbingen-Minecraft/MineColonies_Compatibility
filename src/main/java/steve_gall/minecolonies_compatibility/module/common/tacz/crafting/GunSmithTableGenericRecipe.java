package steve_gall.minecolonies_compatibility.module.common.tacz.crafting;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.tacz.guns.crafting.GunSmithTableRecipe;
import com.tacz.guns.init.ModBlocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemHandlerHelper;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleGenericRecipe;
import steve_gall.minecolonies_compatibility.core.common.crafting.IngredientHelper;

public class GunSmithTableGenericRecipe extends SimpleGenericRecipe
{
	public GunSmithTableGenericRecipe(GunSmithTableRecipe recipe)
	{
		this(recipe.getId(), recipe.getInputs().stream().map(input ->
		{
			List<ItemStack> stacks = new ArrayList<>();

			for (var stack : IngredientHelper.getStacks(input.getIngredient()))
			{
				stacks.add(ItemHandlerHelper.copyStackWithSize(stack, input.getCount()));
			}

			return stacks;
		}).toList(), recipe.getResultItem());
	}

	public GunSmithTableGenericRecipe(ResourceLocation recipeId, List<List<ItemStack>> ingredients, ItemStack output)
	{
		super(recipeId, ingredients, output);
	}

	@Override
	public @NotNull Block getIntermediate()
	{
		return ModBlocks.GUN_SMITH_TABLE.get();
	}

}
